package dao;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import model.GameData;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameDAO implements GameDataAccess {
    private final Connection connection;

    private final Random rand = new Random();

    public GameDAO(Connection connection) {
        this.connection = connection;

        try {
            configureDatabase();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        if (getGameByID(gameData.getGameID()) == null) {
            throw new DataAccessException("Game not found for update");
        }
        String sql = "UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, gameString = ? WHERE gameID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, gameData.getWhiteUsername());
            statement.setString(2, gameData.getBlackUsername());
            statement.setString(3, gameData.getGameName());
            statement.setString(4, new Gson().toJson(gameData.getGame()));
            statement.setInt(5, gameData.getGameID());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public List<GameData> getListGame() throws DataAccessException {
        List<GameData> games = new ArrayList<>();
        String sql = "SELECT * FROM games";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int gameID = resultSet.getInt("gameID");
                String whiteUsername = resultSet.getString("whiteUsername");
                String blackUsername = resultSet.getString("blackUsername");
                String gameName = resultSet.getString("gameName");
                String gameString = resultSet.getString("gameString");

                ChessGame chessGame = new Gson().fromJson(gameString, ChessGame.class);

                games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return games;
    }

    @Override
    public GameData getGameByID(Integer gameID) throws DataAccessException {
        String sql = "SELECT * FROM games WHERE gameID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, gameID);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String whiteUsername = resultSet.getString("whiteUsername");
                    String blackUsername = resultSet.getString("blackUsername");
                    String gameName = resultSet.getString("gameName");
                    String gameString = resultSet.getString("gameString");

                    ChessGame chessGame = new Gson().fromJson(gameString, ChessGame.class);

                    return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM games";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }



    private final String createGamesTableStatement = """
        CREATE TABLE IF NOT EXISTS games (
            `gameID` INT PRIMARY KEY,
            `whiteUsername` VARCHAR(256),
            `blackUsername` VARCHAR(256),
            `gameName` VARCHAR(256) NOT NULL UNIQUE,
            `gameString` TEXT
        )
        """;


    public void createGame(GameData gameData) throws DataAccessException {
        String sql = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, gameString) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            int gameID = (gameData.getGameID() != null) ? gameData.getGameID() : rand.nextInt(Integer.MAX_VALUE);
            statement.setInt(1, gameID);

            statement.setString(2, gameData.getWhiteUsername());
            statement.setString(3, gameData.getBlackUsername());
            statement.setString(4, gameData.getGameName());

            String serializedGame = new Gson().toJson(gameData.getGame());
            statement.setString(5, serializedGame);

            statement.executeUpdate();

            if (gameData.getGameID() == null) {
                gameData.setGameID(gameID);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }




    private void configureDatabase() throws DataAccessException {
        try (Statement statement = connection.createStatement()) {

            statement.executeUpdate(createGamesTableStatement);

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}


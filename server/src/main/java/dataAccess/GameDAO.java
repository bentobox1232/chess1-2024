package dataAccess;

import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import model.GameData;
import chess.ChessGame;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDAO implements GameDataAccess {
    private final Connection connection;

    public GameDAO(Connection connection) {
        this.connection = connection;

        try {
            configureDatabase();
        } catch (DataAccessException e) {
            // Handle the exception or print an error message
            e.printStackTrace();
        }
    }

    public void createGame(GameData gameData) throws DataAccessException {
        String sql = "INSERT INTO games (whiteUsername, blackUsername, gameName, gameString) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, gameData.getWhiteUsername());
            statement.setString(2, gameData.getBlackUsername());
            statement.setString(3, gameData.getGameName());
            statement.setObject(4, gameData.getGame());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedID = generatedKeys.getInt(1);
                    gameData.setGameID(generatedID);
                } else {
                    throw new SQLException("Creating game failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void updateGame(GameData gameData) {

    }

    @Override
    public List<GameData> getListGame() {
        return null;
    }

    @Override
    public GameData getGameByID(Integer gameID) {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }


    public void updateGame(int gameID, String updatedGameString) throws DataAccessException {
        String sql = "UPDATE games SET gameString = ? WHERE gameID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, updatedGameString);
            statement.setInt(2, gameID);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private final String createGamesTableStatement = """
            CREATE TABLE IF NOT EXISTS games (
                `gameID` INT AUTO_INCREMENT PRIMARY KEY,
                `whiteUsername` VARCHAR(256) NOT NULL,
                `blackUsername` VARCHAR(256) NOT NULL,
                `gameName` VARCHAR(256) NOT NULL,
                `gameString` VARCHAR(256) NOT NULL
            )
            """;

    private void configureDatabase() throws DataAccessException {
        try (Statement statement = connection.createStatement()) {

            // Create the games table if it doesn't exist
            statement.executeUpdate(createGamesTableStatement);

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}


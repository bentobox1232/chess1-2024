//package dao;
//
//import dataAccess.DataAccessException;
//import model.GameData;
//import chess.ChessGame;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class GameDAO {
//    private final Connection connection;
//
//    public GameDAO(Connection connection) {
//        this.connection = connection;
//    }
//
//    public void createGame(GameData gameData) throws DataAccessException {
//        String sql = "INSERT INTO games (whiteUsername, blackUsername, gameName, gameString) VALUES (?, ?, ?, ?)";
//        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
//            statement.setString(1, gameData.getWhiteUsername());
//            statement.setString(2, gameData.getBlackUsername());
//            statement.setString(3, gameData.getGameName());
//            statement.setObject(4, gameData.getGame());
//
//            statement.executeUpdate();
//
//            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
//                if (generatedKeys.next()) {
//                    int generatedID = generatedKeys.getInt(1);
//                    gameData.setGameID(generatedID);
//                } else {
//                    throw new SQLException("Creating game failed, no ID obtained.");
//                }
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException(e.getMessage());
//        }
//    }
//
//    public GameData getGame(int gameID) throws DataAccessException {
//        String sql = "SELECT * FROM games WHERE gameID = ?";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setInt(1, gameID);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    String whiteUsername = resultSet.getString("whiteUsername");
//                    String blackUsername = resultSet.getString("blackUsername");
//                    String gameName = resultSet.getString("gameName");
//                    String gameString = resultSet.getString("gameString");
//
//                    Object chessGame = resultSet.getObject("game");
//
//
//
//                    return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
//                }
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException(e.getMessage());
//        }
//        return null; // No game found
//    }
//
//    public List<GameData> listGames() throws DataAccessException {
//        List<GameData> games = new ArrayList<>();
//        String sql = "SELECT * FROM games";
//        try (PreparedStatement statement = connection.prepareStatement(sql);
//             ResultSet resultSet = statement.executeQuery()) {
//            while (resultSet.next()) {
//                int gameID = resultSet.getInt("gameID");
//                String whiteUsername = resultSet.getString("whiteUsername");
//                String blackUsername = resultSet.getString("blackUsername");
//                String gameName = resultSet.getString("gameName");
//                String gameString = resultSet.getString("gameString");
//
//                ChessGame chessGame = new ChessGame();
//                chessGame.setGameString(gameString);
//
//                games.add(new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame));
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException(e.getMessage());
//        }
//        return games;
//    }
//
//    public void updateGame(int gameID, String updatedGameString) throws DataAccessException {
//        String sql = "UPDATE games SET gameString = ? WHERE gameID = ?";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, updatedGameString);
//            statement.setInt(2, gameID);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            throw new DataAccessException(e.getMessage());
//        }
//    }
//}
//

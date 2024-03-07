//package dao;
//
//import dataAccess.DataAccessException;
//import model.AuthData;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class AuthDAO {
//    private final Connection connection;
//
//    public AuthDAO(Connection connection) {
//        this.connection = connection;
//    }
//
//    public void createAuth(AuthData auth) throws DataAccessException {
//        String sql = "INSERT INTO auth (authToken, userId) VALUES (?, ?)";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, auth.getAuthToken());
//            statement.setString(2, auth.getUsername());
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            throw new DataAccessException(e.getMessage());
//        }
//    }
//
//    public AuthData getAuth(String authToken) throws DataAccessException {
//        String sql = "SELECT * FROM auth WHERE authToken = ?";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, authToken);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    String username = resultSet.getString("userId");
//                    return new AuthData(authToken, username);
//                }
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException(e.getMessage());
//        }
//        return null; // No authorization found
//    }
//
//    public void deleteAuth(String authToken) throws DataAccessException {
//        String sql = "DELETE FROM auth WHERE authToken = ?";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, authToken);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            throw new DataAccessException(e.getMessage());
//        }
//    }
//}
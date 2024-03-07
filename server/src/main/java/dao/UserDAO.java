//package dao;
//
//import dataAccess.DataAccessException;
//import model.UserData;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//public class UserDAO {
//    private final Connection connection;
//
//    public UserDAO(Connection connection) {
//        this.connection = connection;
//    }
//
//    public void createUser(UserData userData) throws DataAccessException {
//        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, userData.getUsername());
//            statement.setString(2, userData.getPassword());
//            statement.setString(3, userData.getEmail());
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            throw new DataAccessException(e.getMessage());
//        }
//    }
//
//    public UserData getUser(String username) throws DataAccessException {
//        String sql = "SELECT * FROM users WHERE username = ?";
//        try (PreparedStatement statement = connection.prepareStatement(sql)) {
//            statement.setString(1, username);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    String password = resultSet.getString("password");
//                    String email = resultSet.getString("email");
//                    UserData userData = new UserData();
//                    userData.setUsername(username);
//                    userData.setPassword(password);
//                    userData.setEmail(email);
//                    return userData;
//                }
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException(e.getMessage());
//        }
//        return null; // No user found
//    }
//}

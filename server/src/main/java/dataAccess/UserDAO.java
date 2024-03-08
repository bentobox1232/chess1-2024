package dataAccess;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.UserDataAccess;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO implements UserDataAccess {

    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            // Handle the exception or print an error message
            e.printStackTrace();
        }
    }

    @Override
    public boolean isUsernameTaken(String username) {
        // Implement the logic if needed
        return false;
    }

    public void createUser(UserData userData) throws DataAccessException {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userData.getUsername());
            statement.setString(2, userData.getPassword());
            statement.setString(3, userData.getEmail());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public boolean isCorrectLoginInfo(String username, String password) {
        // Implement the logic if needed
        return false;
    }

    @Override
    public UserData getUserByUsername(String username) throws DataAccessException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String password = resultSet.getString("password");
                    String email = resultSet.getString("email");
                    UserData userData = new UserData();
                    userData.setUsername(username);
                    userData.setPassword(password);
                    userData.setEmail(email);
                    return userData;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null; // No user found
    }

    @Override
    public void clear() throws DataAccessException {
        // Implement the logic if needed
    }

    private final String createUserTableStatement = """
            CREATE TABLE IF NOT EXISTS users (
                `id` INT AUTO_INCREMENT PRIMARY KEY,
                `username` VARCHAR(256) NOT NULL,
                `password` VARCHAR(256) NOT NULL,
                `email` VARCHAR(256) NOT NULL
            )
            """;

    private void configureDatabase() throws DataAccessException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(createUserTableStatement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}

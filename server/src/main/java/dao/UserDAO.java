package dao;

import dataAccess.DataAccessException;
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
            e.printStackTrace();
        }
    }

    @Override
    public boolean isUsernameTaken(String username) throws DataAccessException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
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
    public boolean isCorrectLoginInfo(String username, String password) throws DataAccessException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
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
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM users";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private final String createUserTableStatement = """
            CREATE TABLE IF NOT EXISTS users (
                `id` INT AUTO_INCREMENT PRIMARY KEY,
                `username` VARCHAR(256) NOT NULL UNIQUE,
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

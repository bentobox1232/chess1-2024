package dao;

import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import model.AuthData;

import java.sql.*;

public class AuthDAO implements AuthDataAccess {

    private final Connection connection;

    public AuthDAO(Connection connection) {
        this.connection = connection;
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            // Handle the exception or print an error message
            e.printStackTrace();
        }
    }

    public void createAuth(AuthData auth) throws DataAccessException {
        String sql = "INSERT INTO auth (authToken, userId) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, auth.getAuthToken());
            statement.setString(2, auth.getUsername());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        String sql = "SELECT * FROM auth WHERE authToken = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, authToken);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString("userId");
                    return new AuthData(authToken, username);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return null; // No authorization found
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        String sql = "DELETE FROM auth WHERE authToken = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, authToken);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM auth";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private final String createAuthTableStatement = """
            CREATE TABLE IF NOT EXISTS auth (
                `authToken` VARCHAR(256) PRIMARY KEY,
                `userId` VARCHAR(256) NOT NULL
            )
            """;

    private void configureDatabase() throws DataAccessException {
        try (Statement statement = connection.createStatement()) {

            // Create the auth table if it doesn't exist
            statement.executeUpdate(createAuthTableStatement);

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
package dataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDataAccess implements UserDataAccess {
    // In-memory data structures
    private final Map<String, UserData> usersByUsername;

    public MemoryUserDataAccess() {
        this.usersByUsername = new HashMap<>();
    }

    @Override
    public boolean isUsernameTaken(String username) {
        return usersByUsername.containsKey(username);
    }

    @Override
    public void createUser(UserData userData) {
        usersByUsername.put(userData.getUsername(), userData);
    }

    @Override
    public boolean isCorrectLoginInfo(String username, String password) {
        UserData user = usersByUsername.get(username);
        return user != null && password.equals(user.getPassword());
    }

    @Override
    public UserData getUserByUsername(String username) {
        return usersByUsername.get(username);
    }

    @Override
    public void clear() throws DataAccessException {
        usersByUsername.clear();
    }
}

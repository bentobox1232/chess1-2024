package dataAccess;

import model.UserData;

public interface UserDataAccess {
    boolean isUsernameTaken(String username);

    void createUser(UserData userData);

    boolean isCorrectLoginInfo(String username, String password);
    UserData getUserByUsername(String username);
    void clear() throws DataAccessException;

}

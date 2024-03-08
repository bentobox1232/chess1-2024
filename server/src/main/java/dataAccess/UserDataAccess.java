package dataAccess;

import model.UserData;

public interface UserDataAccess {
    boolean isUsernameTaken(String username);

    void createUser(UserData userData) throws DataAccessException;

    boolean isCorrectLoginInfo(String username, String password);
    UserData getUserByUsername(String username) throws DataAccessException;
    void clear() throws DataAccessException;

}

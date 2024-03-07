package dataAccess;

import model.AuthData;

public interface AuthDataAccess {

    void createAuth(AuthData auth);

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(String authToken);

    void clear() throws DataAccessException;
}

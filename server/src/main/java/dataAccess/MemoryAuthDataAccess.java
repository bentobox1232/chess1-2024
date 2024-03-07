package dataAccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDataAccess implements AuthDataAccess {

    private final Map<String, AuthData> authDataMap;

    public MemoryAuthDataAccess() {
        this.authDataMap = new HashMap<>();
    }

    @Override
    public void createAuth(AuthData auth) {
        authDataMap.put(auth.getAuthToken(), auth);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return authDataMap.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        authDataMap.remove(authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        authDataMap.clear();
    }
}


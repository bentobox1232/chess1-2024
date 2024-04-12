package service;

import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import result.LogoutResult;

public class LogoutService {
    private final AuthDataAccess authDataAccess;

    public LogoutService(AuthDataAccess authDataAccess) {
        this.authDataAccess = authDataAccess;
    }

    public LogoutResult handleLogoutRequest(String authToken) {
        try {
            if(authDataAccess.getAuth(authToken) != null) {
                authDataAccess.deleteAuth(authToken);
                if(authDataAccess.getAuth(authToken) == null) {
                    return new LogoutResult(200, true);
                }
            }
            return new LogoutResult(401, false, "Error: unauthorized");
        } catch (DataAccessException e) {
            return new LogoutResult(500, false, "Error: description");
        }
    }
}

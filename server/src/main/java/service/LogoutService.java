package service;

import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import request.LogoutRequest;
import result.LoginResult;
import result.LogoutResult;

public class LogoutService {
    private AuthDataAccess authDataAccess;

    public LogoutService(AuthDataAccess authDataAccess) {
        this.authDataAccess = authDataAccess;
    }

    public LogoutResult handleLogoutRequest(String authToken) {
        try {
            if(authDataAccess.getAuth(authToken) != null) {
                authDataAccess.deleteAuth(authToken);
                if(authDataAccess.getAuth(authToken) == null) {
                    return new LogoutResult(200);
                }
            }
            return new LogoutResult(401, "Error: unauthorized");
        } catch (DataAccessException e) {
            return new LogoutResult(500, "Error: description");
        }
    }
}

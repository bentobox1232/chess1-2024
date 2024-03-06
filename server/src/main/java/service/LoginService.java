package service;

import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.UserDataAccess;
import model.AuthData;
import model.UserData;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.RegisterResult;

public class LoginService {
    private UserDataAccess userDataAccess;
    private AuthDataAccess authDataAccess;

    public LoginService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public LoginResult handleLoginRequest(LoginRequest loginRequest) {
        try {
            // Validate the input
            if (isValidInput(loginRequest) && userDataAccess.isCorrectLoginInfo(loginRequest.getUsername(), loginRequest.getPassword())) {

                UserData registedUser = userDataAccess.getUserByUsername(loginRequest.getUsername());

                // Generate an authToken for the registered user
                String authToken = generateAuthToken(loginRequest.getUsername());

                return new LoginResult(200, registedUser.getUsername(), authToken);

            } else {
                return new LoginResult(401, "Error: unauthorized");
            }

        } catch (DataAccessException e) {
            return new LoginResult(500, "Error: description");
        }
    }

    private boolean isValidInput(LoginRequest loginRequest) {
        // Add any additional validation logic here
        return loginRequest != null &&
                loginRequest.getUsername() != null &&
                loginRequest.getPassword() != null;
    }

    private String generateAuthToken(String username) throws DataAccessException {
        AuthData authData = new AuthData(username);
        authDataAccess.createAuth(authData);
        return authData.getAuthToken();
    }
}

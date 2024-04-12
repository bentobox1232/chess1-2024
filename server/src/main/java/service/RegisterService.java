package service;

import dataAccess.AuthDataAccess;
import dataAccess.UserDataAccess;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import result.RegisterResult;

public class RegisterService {
    private final UserDataAccess userDataAccess;
    private final AuthDataAccess authDataAccess;

    public RegisterService(UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public RegisterResult handleRegisterRequest(RegisterRequest registerRequest) {
        try {
            // Validate the input
            if (isValidInput(registerRequest)) {
                // Check if the username is already taken
                if (userDataAccess.isUsernameTaken(registerRequest.getUsername())) {
                    return new RegisterResult(403, false, "Error: already taken");
                }

                // Create a new user in the database
                UserData newUser = new UserData(
                        registerRequest.getUsername(),
                        registerRequest.getPassword(),
                        registerRequest.getEmail()
                );

                userDataAccess.createUser(newUser);

                UserData registedUser = userDataAccess.getUserByUsername(newUser.getUsername());

                // Generate an authToken for the registered user
                String authToken = generateAuthToken(newUser.getUsername());

                return new RegisterResult(200, true, authToken);
            } else {
                return new RegisterResult(400, false, "Error: bad request");
            }
        } catch (DataAccessException e) {
            return new RegisterResult(500, false, "Error: description");
        }
    }

    private boolean isValidInput(RegisterRequest registerRequest) {
        // Add any additional validation logic here
        return registerRequest != null &&
                registerRequest.getUsername() != null &&
                registerRequest.getPassword() != null &&
                registerRequest.getEmail() != null;
    }

    private String generateAuthToken(String username) throws DataAccessException {
        AuthData authData = new AuthData(username);
        authDataAccess.createAuth(authData);
        return authData.getAuthToken();
    }
}


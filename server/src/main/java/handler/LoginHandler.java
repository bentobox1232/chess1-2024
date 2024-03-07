package handler;

import com.google.gson.Gson;
import dataAccess.AuthDataAccess;
import dataAccess.UserDataAccess;
import request.LoginRequest;
import result.LoginResult;
import service.LoginService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LoginHandler implements Route {

    private Gson gson;

    private UserDataAccess userDataAccess;

    private AuthDataAccess authDataAccess;

    public LoginHandler(UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        this.authDataAccess = authDataAccess;
        this.userDataAccess = userDataAccess;
    }
    @Override
    public Object handle(Request request, Response response) {
        gson = new Gson();
        // Parse request
        LoginRequest loginRequest = gson.fromJson(request.body(), LoginRequest.class);


        LoginService loginService = new LoginService(userDataAccess, authDataAccess);
        LoginResult loginResult = loginService.handleLoginRequest(loginRequest);

        // Return response
        response.type("application/json");
        response.status(loginResult.getErrorCode());
        return gson.toJson(loginResult);
    }
}

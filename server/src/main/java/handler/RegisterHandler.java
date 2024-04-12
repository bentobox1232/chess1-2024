package handler;

import com.google.gson.Gson;
import dataAccess.*;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegisterService;
import spark.Request;
import spark.Response;
import spark.Route;


public class RegisterHandler implements Route {

    private Gson gson;

    private final UserDataAccess userDataAccess;

    private final AuthDataAccess authDataAccess;


    public RegisterHandler(UserDataAccess userDataAccess, AuthDataAccess authDataAccess) {
        this.authDataAccess = authDataAccess;
        this.userDataAccess = userDataAccess;
    }

    @Override
    public Object handle(Request request, Response response) {

        gson = new Gson();
        // Parse request
        RegisterRequest registerRequest = gson.fromJson(request.body(), RegisterRequest.class);


        RegisterService registerService = new RegisterService(userDataAccess, authDataAccess);
        RegisterResult registerResult = registerService.handleRegisterRequest(registerRequest);

        // Return response
        response.type("application/json");
        response.status(registerResult.getErrorCode());
        return gson.toJson(registerResult);
    }

}

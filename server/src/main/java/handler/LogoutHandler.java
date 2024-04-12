package handler;

import com.google.gson.Gson;
import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import request.LogoutRequest;
import result.LogoutResult;
import service.LogoutService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {
    private Gson gson;
    private final AuthDataAccess authDataAccess;

    public LogoutHandler(AuthDataAccess authDataAccess) {
        this.authDataAccess = authDataAccess;
    }

    @Override
    public Object handle(Request request, Response response) {
        gson = new Gson();

        String authToken = request.headers("Authorization");

        LogoutService logoutService = new LogoutService(authDataAccess);
        LogoutResult logoutResult = logoutService.handleLogoutRequest(authToken);

        response.type("application/json");
        response.status(logoutResult.getErrorCode());
        return gson.toJson(logoutResult);
    }
}

package handler;

import com.google.gson.Gson;
import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import dataAccess.UserDataAccess;
import result.ClearResult;
import service.ClearService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;

public class ClearHandler implements Route {

    private ClearService clearDataService;
    private Gson gson;
    private final AuthDataAccess authDataAccess;
    private final GameDataAccess gameDataAccess;
    private final UserDataAccess userDataAccess;

    public ClearHandler(AuthDataAccess authDataAccess, GameDataAccess gameDataAccess, UserDataAccess userDataAccess) {
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
        this.userDataAccess = userDataAccess;
    }

    @Override
    public Object handle(Request request, Response response) {
        // Parse request if needed
        // Clear data using ClearDataService
        gson = new Gson();
        ClearService clearDataService = new ClearService(authDataAccess, gameDataAccess, userDataAccess);
        ClearResult clearDataResponse = clearDataService.clear();

        // Return success response
        response.status(clearDataResponse.getErrorCode());
        return gson.toJson(clearDataResponse);
    }
}

package handler;

import com.google.gson.Gson;
import dataAccess.AuthDataAccess;
import dataAccess.GameDataAccess;
import request.CreateRequest;
import result.CreateResult;
import service.CreateService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateHandler implements Route {
    private Gson gson;
    private GameDataAccess gameDataAccess;
    private AuthDataAccess authDataAccess;

    public CreateHandler(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }
    @Override
    public Object handle(Request request, Response response) {
        gson = new Gson();

        String authToken = request.headers("Authorization");
        CreateRequest createRequest = gson.fromJson(request.body(), CreateRequest.class);

        CreateService createService = new CreateService(gameDataAccess, authDataAccess);
        CreateResult createResult = createService.handleCreateRequest(createRequest, authToken);

        response.type("application/json");
        response.status(createResult.getErrorCode());
        return gson.toJson(createResult);
    }
}

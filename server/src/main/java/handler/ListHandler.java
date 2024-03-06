package handler;

import com.google.gson.Gson;
import dataAccess.AuthDataAccess;
import dataAccess.GameDataAccess;
import request.ListRequest;
import result.ListResult;
import service.ListService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ListHandler implements Route {
    private Gson gson;
    private GameDataAccess gameDataAccess;
    private AuthDataAccess authDataAccess;

    public ListHandler(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }
    @Override
    public Object handle(Request request, Response response) {
        gson = new Gson();

        String authToken = request.headers("Authorization");
        ListRequest listRequest = gson.fromJson(request.body(), ListRequest.class);

        ListService listService = new ListService(gameDataAccess, authDataAccess);
        ListResult listResult = listService.handleListRequest(listRequest, authToken);

        response.type("application/json");
        response.status(listResult.getErrorCode());
        return gson.toJson(listResult);
    }
}

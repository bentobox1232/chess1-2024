package handler;

import com.google.gson.Gson;
import dataAccess.AuthDataAccess;
import dataAccess.GameDataAccess;
import request.JoinRequest;
import result.JoinResult;
import service.JoinService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinHandler implements Route {
    private Gson gson;
    private GameDataAccess gameDataAccess;
    private AuthDataAccess authDataAccess;

    public JoinHandler(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    @Override
    public Object handle(Request request, Response response) {
        gson = new Gson();

        String authToken = request.headers("Authorization");
        JoinRequest joinRequest = gson.fromJson(request.body(), JoinRequest.class);

        JoinService joinService = new JoinService(gameDataAccess, authDataAccess);
        JoinResult joinResult = joinService.handleJoinRequest(joinRequest, authToken);

        response.type("application/json");
        response.status(joinResult.getErrorCode());
        return gson.toJson(joinResult);
    }
}

package service;

import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import model.GameData;
import request.ListRequest;
import result.ListResult;

import java.util.List;

public class ListService {
    private GameDataAccess gameDataAccess;
    private AuthDataAccess authDataAccess;

    public ListService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public ListResult handleListRequest(ListRequest listRequest, String authToken) {
        try {
            if(!isAuthorized(authToken)) {
                return new ListResult(401, false, "Error: unauthorized");
            } else {
                List<GameData> games = gameDataAccess.getListGame();
                return new ListResult(200, true, games);
            }

        } catch (DataAccessException e) {
            return new ListResult(500, false, "Error: description");
        }

    }

    private boolean isAuthorized(String authToken) throws DataAccessException {
        if(authDataAccess.getAuth(authToken) != null) {
            return true;
        }
        return false;
    }
}

package service;

import chess.ChessGame;
import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import model.GameData;
import request.CreateRequest;
import result.CreateResult;

import java.util.Random;

public class CreateService {
    private Random rand = new Random();
    private GameDataAccess gameDataAccess;
    private AuthDataAccess authDataAccess;

    public CreateService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public CreateResult handleCreateRequest(CreateRequest createRequest, String authToken) {
        try {
            if(isValidRequest(createRequest)) {
                // Validate authtoken
                if(!isAuthorized(createRequest.getAuthToken()) && !isAuthorized(authToken)) {
                    return new CreateResult(401, false, "Error: unauthorized");
                }

                Integer gameId = rand.nextInt(Integer.MAX_VALUE);

                GameData newGame = new GameData(
                        createRequest.getGameName(),
                        gameId,
                        new ChessGame()
                );

                gameDataAccess.createGame(newGame);

                return new CreateResult(200, true, newGame.getGameID());

            } else{
                return new CreateResult(400, false, "Error: bad request");
            }
        } catch (DataAccessException e) {
            return new CreateResult(500, false, "Error: description");
        }
    }

    private boolean isValidRequest(CreateRequest createRequest) {
        return createRequest != null && createRequest.getGameName() != null;
    }

    private boolean isAuthorized(String authToken) throws DataAccessException{
        if(authDataAccess.getAuth(authToken) != null) {
            return true;
        }
        return false;
    }
}

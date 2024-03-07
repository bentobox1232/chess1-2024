package service;

import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import model.GameData;
import request.JoinRequest;
import request.JoinRequest;
import result.CreateResult;
import result.JoinResult;

public class JoinService {
    private GameDataAccess gameDataAccess;
    private AuthDataAccess authDataAccess;
    public JoinService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }
    public JoinResult handleJoinRequest(JoinRequest joinRequest, String authToken) {
        try {
            if(isValidRequest(joinRequest)) {
                if(!isAuthorized(authToken)) {
                    return new JoinResult(401, "Error: unauthorized");
                }

                if(joinRequest.getPlayerColor() != null) {
                    if(colorTaken(joinRequest)) {
                        return new JoinResult(403,  "Error: already taken");
                    }
                }


                GameData game = gameDataAccess.getGameByID(joinRequest.getGameID());
                setPlayerColor(game, joinRequest, authToken);
                gameDataAccess.updateGame(game);

                return new JoinResult(200);

            } else {
                return new JoinResult(400, "Error: bad request");
            }

        } catch (DataAccessException e) {
            return new JoinResult(500, "Error: description");
        }
    }

    private boolean isAuthorized(String authToken) throws DataAccessException {
        if(authDataAccess.getAuth(authToken) != null) {
            return true;
        }
        return false;
    }

    private void setPlayerColor(GameData game, JoinRequest joinRequest, String authToken) throws DataAccessException {
        String color = joinRequest.getPlayerColor();
        String username = authDataAccess.getAuth(authToken).getUsername();
        if ("BLACK".equals(color)) {
            game.setBlackUsername(username);
        } else if ("WHITE".equals(color)) {
            game.setWhiteUsername(username);
        }
    }

    private boolean isValidRequest(JoinRequest joinRequest) {
        return joinRequest != null && joinRequest.getGameID() != null && joinRequest.getGameID() != 0;
    }

    private boolean colorTaken(JoinRequest joinRequest) {
        GameData game = gameDataAccess.getGameByID(joinRequest.getGameID());
        String color = joinRequest.getPlayerColor();
        if (game != null) {
            if ("BLACK".equals(color) && game.getBlackUsername() == null) {
                return false;
            } else if ("WHITE".equals(color) && game.getWhiteUsername() == null) {
                return false;
            }
        }
        return true;
    }


}

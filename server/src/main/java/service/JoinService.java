package service;

import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import model.GameData;
import request.JoinRequest;
import result.JoinResult;

public class JoinService {
    private GameDataAccess gameDataAccess;
    private AuthDataAccess authDataAccess;
    public JoinService(GameDataAccess gameDataAccess, AuthDataAccess authDataAccess) {
        this.gameDataAccess = gameDataAccess;
        this.authDataAccess = authDataAccess;
    }
    public JoinResult handleJoinRequest(JoinRequest joinRequest) {
        try {
            if(isValidRequest(joinRequest)) {
                if(!isAuthorized(joinRequest.getAuthToken())) {
                    return new JoinResult(401, false, "Error: unauthorized");
                }

                if(joinRequest.getPlayerColor() != null) {
                    if(colorTaken(joinRequest)) {
                        return new JoinResult(403, false, "Error: already taken");
                    }
                }


                GameData game = gameDataAccess.getGameByID(joinRequest.getGameID());
                setPlayerColor(game, joinRequest);
                gameDataAccess.updateGame(game);

                return new JoinResult(200, true);

            } else {
                return new JoinResult(400, false, "Error: bad request");
            }

        } catch (DataAccessException e) {
            return new JoinResult(500, false,"Error: description");
        }
    }

    private boolean isAuthorized(String authToken) throws DataAccessException {
        if(authDataAccess.getAuth(authToken) != null) {
            return true;
        }
        return false;
    }

    private void setPlayerColor(GameData game, JoinRequest joinRequest) throws DataAccessException {
        String color = joinRequest.getPlayerColor();
        String username = authDataAccess.getAuth(joinRequest.getAuthToken()).getUsername();
        if ("BLACK".equals(color)) {
            game.setBlackUsername(username);
        } else if ("WHITE".equals(color)) {
            game.setWhiteUsername(username);
        }
    }

    private boolean isValidRequest(JoinRequest joinRequest) {
        return joinRequest != null && joinRequest.getGameID() != null && joinRequest.getGameID() != 0;
    }

    private boolean colorTaken(JoinRequest joinRequest) throws DataAccessException {
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

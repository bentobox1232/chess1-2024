package webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dao.AuthDAO;
import dao.GameDAO;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Map;
import java.util.Objects;

public class GameService {

    private static final Gson gson = new Gson();


    public static void joinPlayer(String authToken, Integer gameID, ChessGame.TeamColor teamColor, WebSocketSessions webSocketSessions){
        try {
            var connection = DatabaseManager.getConnection();
            GameDAO gameDao = new GameDAO(connection);
            GameData gameData = gameDao.getGameByID(gameID);
            AuthDAO authDao = new AuthDAO(connection);
            AuthData authData = authDao.getAuth(authToken);

            if (gameData == null || authData == null){
                Error error = new Error("Error: Invalid game ID or authToken");
                sendMessage(webSocketSessions, gameID, authToken, error);
                return;
            }
            if ((teamColor == ChessGame.TeamColor.WHITE && !Objects.equals(gameData.getWhiteUsername(), authData.getUsername())) || (teamColor == ChessGame.TeamColor.BLACK && !Objects.equals(gameData.getBlackUsername(), authData.getUsername()))){
                Error error = new Error("Error: Spot already taken");
                sendMessage(webSocketSessions, gameID, authToken, error);
                return;
            }
            if (gameData.getGame() == null){
                Error error = new Error("Error: Game has already ended");
                sendMessage(webSocketSessions, gameID, authToken, error);
                return;
            }

            LoadGame loadGame = new LoadGame(gameData.getGame());
            sendMessage(webSocketSessions, gameID, authToken, loadGame);

            Notification notification = new Notification(authData.getUsername() + "joined game as" + teamColor.name());
            broadcastMessage(webSocketSessions, gameID, authToken, notification);

        } catch (DataAccessException e) {
            Error error = new Error("Error: Invalid Game ID or Game Does Not Exist");
            sendMessage(webSocketSessions, gameID, authToken, error);
        }
    }

    private static void sendMessage(WebSocketSessions webSocketSessions, Integer gameID, String authToken, ServerMessage message) {
        Session session = webSocketSessions.getSessionsForGame(gameID).get(authToken);
        if (session != null) {
            String jsonMessage = gson.toJson(message);
            try {
                session.getRemote().sendString(jsonMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Session not found for gameID: " + gameID + " and authToken: " + authToken);
        }
    }

    private static void broadcastMessage(WebSocketSessions webSocketSessions, Integer gameID, String exceptThisAuthToken, ServerMessage message) {
        for (Map.Entry<String, Session> entry : webSocketSessions.getSessionsForGame(gameID).entrySet()) {
            if (!entry.getKey().equals(exceptThisAuthToken)) {
                String jsonMessage = gson.toJson(message);
                try {
                    entry.getValue().getRemote().sendString(jsonMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

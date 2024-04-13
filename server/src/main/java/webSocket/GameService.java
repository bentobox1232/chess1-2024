package webSocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
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

import java.sql.Connection;
import java.util.Map;
import java.util.Objects;

public class GameService {

    private static final Gson gson = new Gson();
    private final GameDAO gameDao;
    private final AuthDAO authDao;

    public GameService() {
        Connection connection;
        try {
            connection = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        gameDao = new GameDAO(connection);
        authDao = new AuthDAO(connection);
    }

    public void joinPlayer(String authToken, Integer gameID, ChessGame.TeamColor teamColor, WebSocketSessions webSocketSessions) {
        try {
            GameData gameData = getGameData(gameID);
            AuthData authData = getAuthData(authToken);

            if (gameData == null || authData == null) {
                handleErrorMessage(webSocketSessions, gameID, authToken, "Invalid game ID or authToken");
                return;
            }

            if (!isSpotAvailable(gameData, authData.getUsername(), teamColor)) {
                handleErrorMessage(webSocketSessions, gameID, authToken, "Spot already taken");
                return;
            }

            if (gameData.getGame() == null) {
                handleErrorMessage(webSocketSessions, gameID, authToken, "Game has already ended");
                return;
            }

            LoadGame loadGame = new LoadGame(gameData.getGame());
            sendMessage(webSocketSessions, gameID, authToken, loadGame);

            Notification notification = new Notification(authData.getUsername() + " joined game as " + teamColor.name());
            broadcastMessage(webSocketSessions, gameID, authToken, notification);

        } catch (DataAccessException e) {
            handleErrorMessage(webSocketSessions, gameID, authToken, "Invalid Game ID or Game Does Not Exist");
        }
    }

    public void joinObserver(String authToken, Integer gameID, WebSocketSessions webSocketSessions) {
        try {
            GameData gameData = getGameData(gameID);
            AuthData authData = getAuthData(authToken);

            if (gameData == null || authData == null) {
                handleErrorMessage(webSocketSessions, gameID, authToken, "Invalid game ID or authToken");
                return;
            }

            if (gameData.getGame() == null) {
                handleErrorMessage(webSocketSessions, gameID, authToken, "Game has already ended");
                return;
            }

            LoadGame loadGame = new LoadGame(gameData.getGame());
            sendMessage(webSocketSessions, gameID, authToken, loadGame);

            Notification notification = new Notification(authData.getUsername() + " joined game as an observer");
            broadcastMessage(webSocketSessions, gameID, authToken, notification);

        } catch (DataAccessException e) {
            handleErrorMessage(webSocketSessions, gameID, authToken, "Invalid Game ID or Game Does Not Exist");
        }
    }

    public void leaveGame(String authToken, Integer gameID, WebSocketSessions webSocketSessions) {
        try {
            GameData gameData = getGameData(gameID);
            AuthData authData = getAuthData(authToken);

            String whiteUsername = gameData.getWhiteUsername();
            String blackUsername = gameData.getBlackUsername();
            if (Objects.equals(authData.getUsername(), gameData.getBlackUsername())) {
                blackUsername = null;
            }
            if (Objects.equals(authData.getUsername(), gameData.getWhiteUsername())) {
                whiteUsername = null;
            }

            gameDao.updateGame(new GameData(gameData.getGameID(), whiteUsername, blackUsername, gameData.getGameName(), gameData.getGame()));

            Notification notification = new Notification(authData.getUsername() + " has left the game.");
            broadcastMessage(webSocketSessions, gameID, authToken, notification);

            webSocketSessions.removeSessionFromGame(gameID, authToken);

        } catch (DataAccessException e) {
            handleErrorMessage(webSocketSessions, gameID, authToken, "Invalid Game ID or Game Does Not Exist");
        }
    }

    public void resignGame(String authToken, Integer gameID, WebSocketSessions webSocketSessions) {
        try {
            GameData gameData = getGameData(gameID);
            AuthData authData = getAuthData(authToken);

            if (gameData.getGame() == null) {
                handleErrorMessage(webSocketSessions, gameID, authToken, "Game has already ended");
                return;
            }

            if (!Objects.equals(authData.getUsername(), gameData.getBlackUsername()) && !Objects.equals(authData.getUsername(), gameData.getWhiteUsername())) {
                handleErrorMessage(webSocketSessions, gameID, authToken, "Observers cannot resign. Type leave to leave.");
                return;
            }

            gameDao.updateGame(new GameData(gameData.getGameID(), gameData.getWhiteUsername(), gameData.getBlackUsername(), "GAME ENDED (" + gameData.getGameName() + ")", null));

            Notification notification = new Notification(authData.getUsername() + " resigned from the game");
            sendMessage(webSocketSessions, gameID, authToken, notification);
            broadcastMessage(webSocketSessions, gameID, authToken, notification);

            Map<String, Session> sessions = webSocketSessions.getSessionsForGame(gameID);
            for (String authorization : sessions.keySet()) {
                webSocketSessions.removeSessionFromGame(gameID, authorization);
            }

        } catch (DataAccessException e) {
            handleErrorMessage(webSocketSessions, gameID, authToken, "Invalid Game ID or Game Does Not Exist");
        }
    }

    public void makeMove(String authToken, Integer gameID, ChessMove move, WebSocketSessions webSocketSessions) {
        try {
            GameData gameData = getGameData(gameID);
            AuthData authData = getAuthData(authToken);

            if (gameData.getGame() == null) {
                handleErrorMessage(webSocketSessions, gameID, authToken, "Game has already ended");
                return;
            }

            if ((!Objects.equals(authData.getUsername(), gameData.getWhiteUsername()) && gameData.getGame().getTeamTurn() == ChessGame.TeamColor.WHITE) ||
                    (!Objects.equals(authData.getUsername(), gameData.getBlackUsername()) && gameData.getGame().getTeamTurn() == ChessGame.TeamColor.BLACK)) {
                handleErrorMessage(webSocketSessions, gameID, authToken, "It is not your turn to move.");
                return;
            }

            if (gameData.getGame().isInCheckmate(ChessGame.TeamColor.WHITE) || gameData.getGame().isInCheckmate(ChessGame.TeamColor.BLACK)) {
                handleErrorMessage(webSocketSessions, gameID, authToken, "Game is in checkmate and no other moves are allowed.");
                return;
            }

            if (!gameData.getGame().validMoves(move.getStartPosition()).contains(move)) {
                handleErrorMessage(webSocketSessions, gameID, authToken, "Invalid move. Make sure you are not in check and your syntax is correct by typing help.");
                return;
            }

            gameData.getGame().makeMove(move);

            gameDao.updateGame(new GameData(gameID, gameData.getWhiteUsername(), gameData.getBlackUsername(), gameData.getGameName(), gameData.getGame()));

            LoadGame loadGame = new LoadGame(gameData.getGame());
            sendMessage(webSocketSessions, gameID, authToken, loadGame);
            broadcastMessage(webSocketSessions, gameID, authToken, loadGame);

            Notification notification = new Notification(authData.getUsername() + " moved a piece!");
            broadcastMessage(webSocketSessions, gameID, authToken, notification);

            if (gameData.getGame().isInCheck(ChessGame.TeamColor.WHITE)) {
                handleCheckNotification(webSocketSessions, gameID, authToken, "White");
            } else if (gameData.getGame().isInCheck(ChessGame.TeamColor.BLACK)) {
                handleCheckNotification(webSocketSessions, gameID, authToken, "Black");
            }

        } catch (DataAccessException e) {
            handleErrorMessage(webSocketSessions, gameID, authToken, "Invalid Game ID or Game Does Not Exist");
        } catch (InvalidMoveException e) {
            handleErrorMessage(webSocketSessions, gameID, authToken, "Invalid Move. Make sure it is your turn, and your letter columns and number rows are formatted correctly.");
        }
    }

    private GameData getGameData(Integer gameID) throws DataAccessException {
        return gameDao.getGameByID(gameID);
    }

    private AuthData getAuthData(String authToken) throws DataAccessException {
        return authDao.getAuth(authToken);
    }

    private boolean isSpotAvailable(GameData gameData, String username, ChessGame.TeamColor teamColor) {
        return (teamColor == ChessGame.TeamColor.WHITE && Objects.equals(gameData.getWhiteUsername(), username)) ||
                (teamColor == ChessGame.TeamColor.BLACK && Objects.equals(gameData.getBlackUsername(), username));
    }

    private void handleErrorMessage(WebSocketSessions webSocketSessions, Integer gameID, String authToken, String errorMessage) {
        Error error = new Error("Error: " + errorMessage);
        sendMessage(webSocketSessions, gameID, authToken, error);
    }

    private void sendMessage(WebSocketSessions webSocketSessions, Integer gameID, String authToken, ServerMessage message) {
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

    private void broadcastMessage(WebSocketSessions webSocketSessions, Integer gameID, String exceptThisAuthToken, ServerMessage message) {
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

    private void handleCheckNotification(WebSocketSessions webSocketSessions, Integer gameID, String authToken, String color) {
        Notification gameEvent;
        if (color.equals("White")) {
            gameEvent = new Notification("White is currently in check!");
        } else {
            gameEvent = new Notification("Black is currently in check!");
        }

        sendMessage(webSocketSessions, gameID, authToken, gameEvent);
        broadcastMessage(webSocketSessions, gameID, authToken, gameEvent);
    }
}

package webSocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
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

    public static Connection connection;

    public static GameDAO gameDao;

    public static AuthDAO authDao;

    static {
        try {
            connection = DatabaseManager.getConnection();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public GameService() {

        gameDao = new GameDAO(connection);
        authDao = new AuthDAO(connection);

    }

    public static void joinPlayer(String authToken, Integer gameID, ChessGame.TeamColor teamColor, WebSocketSessions webSocketSessions){
        try {

            GameData gameData = gameDao.getGameByID(gameID);
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

    public static void joinObserver(String authToken, Integer gameID, WebSocketSessions webSocketSessions){
        try {
            GameData gameData = gameDao.getGameByID(gameID);
            AuthData authData = authDao.getAuth(authToken);

            if (gameData == null || authData == null){
                Error error = new Error("Error: Invalid game ID or authToken");
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

            Notification notification = new Notification(authData.getUsername() + " joined game as an observer");
            broadcastMessage(webSocketSessions, gameID, authToken, notification);

        } catch (DataAccessException e) {
            Error error = new Error("Error: Invalid Game ID or Game Does Not Exist");
            sendMessage(webSocketSessions, gameID, authToken, error);
        }
    }




    public static void leaveGame(String authToken, Integer gameID, WebSocketSessions webSocketSessions){
        try {
            GameData gameData = gameDao.getGameByID(gameID);
            AuthData authData = authDao.getAuth(authToken);

            String whiteUsername = gameData.getWhiteUsername();
            String blackUsername = gameData.getBlackUsername();
            if (Objects.equals(authData.getUsername(), gameData.getBlackUsername())){
                blackUsername = null;
            }
            if (Objects.equals(authData.getUsername(), gameData.getWhiteUsername())) {
                whiteUsername = null;
            }

            gameDao.updateGame(new GameData(gameData.getGameID(), whiteUsername, blackUsername, gameData.getGameName(), gameData.getGame()));


            Notification notification = new Notification(authData.getUsername() + "has left the game.");
            broadcastMessage(webSocketSessions, gameID, authToken, notification);

            webSocketSessions.removeSessionFromGame(gameID, authToken);

        } catch (DataAccessException e) {
            Error error = new Error("Error: Invalid Game ID or Game Does Not Exist");
            sendMessage(webSocketSessions, gameID, authToken, error);
        }
    }

    public static void resignGame(String authToken, Integer gameID, WebSocketSessions webSocketSessions){
        try {
            GameData gameData = gameDao.getGameByID(gameID);
            AuthData authData = authDao.getAuth(authToken);

            if (gameData.getGame() == null){
                Error error = new Error("Error: Game has already ended");
                sendMessage(webSocketSessions, gameID, authToken, error);
                return;
            }
            if (!Objects.equals(authData.getUsername(), gameData.getBlackUsername()) && !Objects.equals(authData.getUsername(), gameData.getWhiteUsername())){
                Error error = new Error("Error: Observers cannot resign. type leave to leave.");
                sendMessage(webSocketSessions, gameID, authToken, error);
                return;
            }

            gameDao.updateGame(new GameData(gameData.getGameID(), gameData.getWhiteUsername(), gameData.getBlackUsername(), "GAME ENDED (" + gameData.getGameName() + ")", null));

            Notification notification = new Notification(authData.getUsername() + "resigned from the game");
            sendMessage(webSocketSessions, gameID, authToken, notification);
            broadcastMessage(webSocketSessions, gameID, authToken, notification);

            Map<String, Session> sessions = webSocketSessions.getSessionsForGame(gameID);
            for (String authorization : sessions.keySet()){
                webSocketSessions.removeSessionFromGame(gameID, authorization);
            }


        } catch (DataAccessException e) {
            Error error = new Error("Error: Invalid Game ID or Game Does Not Exist");
            sendMessage(webSocketSessions, gameID, authToken, error);
        }
    }

    public static void makeMove(String authToken, Integer gameID, ChessMove move, WebSocketSessions webSocketSessions){
        try {
            GameData gameData = gameDao.getGameByID(gameID);
            AuthData authData = authDao.getAuth(authToken);

            if (gameData.getGame() == null){
                Error error = new Error("Error: Game has already ended");
                sendMessage(webSocketSessions, gameID, authToken, error);
                return;
            }
            if ((!Objects.equals(authData.getUsername(), gameData.getWhiteUsername()) && gameData.getGame().getTeamTurn() == ChessGame.TeamColor.WHITE) || (!Objects.equals(authData.getUsername(), gameData.getBlackUsername()) && gameData.getGame().getTeamTurn() == ChessGame.TeamColor.BLACK)){
                Error error = new Error("Error: It is not your turn to move.");
                sendMessage(webSocketSessions, gameID, authToken, error);
                return;
            }

            if (gameData.getGame().isInCheckmate(ChessGame.TeamColor.WHITE) || gameData.getGame().isInCheckmate(ChessGame.TeamColor.BLACK)){
                Error error = new Error("Error: Game is in checkmate and no other moves are allowed.");
                sendMessage(webSocketSessions, gameID, authToken, error);
                return;
            }

            ChessMove correctMove = new ChessMove( new ChessPosition(move.getStartPosition().getRow()+ 1, move.getStartPosition().getColumn() + 1)
                    ,new ChessPosition(move.getEndPosition().getRow()+ 1, move.getEndPosition().getColumn() + 1), move.getPromotionPiece());



            if(gameData.getGame().validMoves(correctMove.getStartPosition()).contains(correctMove)) {
                gameData.getGame().makeMove(correctMove);
            } else {
                Error error = new Error("Error: Invalid move. Make sure you are not in check and your syntax is correct by typing help.");
                sendMessage(webSocketSessions, gameID, authToken, error);
                return;
            }

            gameDao.updateGame(new GameData(gameID, gameData.getWhiteUsername(), gameData.getBlackUsername(), gameData.getGameName(), gameData.getGame()));

            LoadGame loadGame = new LoadGame(gameData.getGame());
            sendMessage(webSocketSessions, gameID, authToken, loadGame);
            broadcastMessage(webSocketSessions, gameID, authToken, loadGame);

            Notification notification = new Notification(authData.getUsername() + " moved a piece!");
            broadcastMessage(webSocketSessions, gameID, authToken, notification);


            if (gameData.getGame().isInCheck(ChessGame.TeamColor.WHITE) ){
                Notification gameEvent;
                if (gameData.getGame().isInCheckmate(ChessGame.TeamColor.WHITE) ){
                    gameEvent = new Notification("White is currently in checkmate.\nGAME OVER");
                } else {
                    gameEvent = new Notification("White is currently in check!");
                }

                sendMessage(webSocketSessions, gameID, authToken, gameEvent);
                broadcastMessage(webSocketSessions, gameID, authToken, gameEvent);

            } else if (gameData.getGame().isInCheck(ChessGame.TeamColor.BLACK)){
                Notification gameEvent;
                if (gameData.getGame().isInCheckmate(ChessGame.TeamColor.BLACK)){
                    gameEvent = new Notification("Black is currently in checkmate.\nGAME OVER");
                } else {
                    gameEvent = new Notification("Black is currently in check!");
                }

                sendMessage(webSocketSessions, gameID, authToken, gameEvent);
                broadcastMessage(webSocketSessions, gameID, authToken, gameEvent);
            }


        } catch (DataAccessException e) {
            Error error = new Error("Error: Invalid Game ID or Game Does Not Exist");
            sendMessage(webSocketSessions, gameID, authToken, error);
        } catch (InvalidMoveException e){
            Error error = new Error("Error: Invalid Move. Make sure it is your turn, and your letter columns and number rows are formatted correctly.");
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

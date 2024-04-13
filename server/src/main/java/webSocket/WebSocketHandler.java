package webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.*;

@WebSocket
public class WebSocketHandler {

    private final WebSocketSessions webSocketSessions = new WebSocketSessions();
    private final GameService gameService = new GameService();
    private final Gson gson = new Gson();

    public WebSocketHandler() {}

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println("Message received from client: " + message);
        UserGameCommand userCommand = gson.fromJson(message, UserGameCommand.class);

        webSocketSessions.addSessionToGame(userCommand.getGameID(), userCommand.getAuthString(), session);

        switch (userCommand.getCommandType()) {
            case MAKE_MOVE:
                handleMakeMove(message);
                break;
            case JOIN_OBSERVER:
                handleJoinObserver(message);
                break;
            case JOIN_PLAYER:
                handleJoinPlayer(message);
                break;
            case LEAVE:
                handleLeave(message);
                break;
            case RESIGN:
                handleResign(message);
                break;
            default:
                System.out.println("Unknown command type");
        }
    }

    private void handleMakeMove(String message) {
        MakeMove command = gson.fromJson(message, MakeMove.class);
        gameService.makeMove(command.getAuthString(), command.getGameID(), command.getMove(), webSocketSessions);
    }

    private void handleJoinObserver(String message) {
        JoinObserver command = gson.fromJson(message, JoinObserver.class);
        gameService.joinObserver(command.getAuthString(), command.getGameID(), webSocketSessions);
    }

    private void handleJoinPlayer(String message) {
        JoinPlayer command = gson.fromJson(message, JoinPlayer.class);
        gameService.joinPlayer(command.getAuthString(), command.getGameID(), command.getPlayerColor(), webSocketSessions);
    }

    private void handleLeave(String message) {
        Leave command = gson.fromJson(message, Leave.class);
        gameService.leaveGame(command.getAuthString(), command.getGameID(), webSocketSessions);
    }

    private void handleResign(String message) {
        Resign command = gson.fromJson(message, Resign.class);
        gameService.resignGame(command.getAuthString(), command.getGameID(), webSocketSessions);
    }
}

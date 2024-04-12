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

    public WebSocketHandler() {}


    @OnWebSocketMessage
    public void onMessage(Session session, String message) {

        System.out.println("Message received from client: " + message);
        UserGameCommand msg = new Gson().fromJson(message, UserGameCommand.class);

        webSocketSessions.addSessionToGame(msg.getGameID(), msg.getAuthString(), session);


        if (msg.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE) {
            MakeMove command = new Gson().fromJson(message, MakeMove.class);
            GameService.makeMove(msg.getCommandType().toString(), command.getAuthString(), command.getGameID(), command.getMove(), webSocketSessions);

        } else if (msg.getCommandType() == UserGameCommand.CommandType.JOIN_OBSERVER) {
            JoinObserver commandObj = new Gson().fromJson(message, JoinObserver.class);
            GameService.joinObserver(commandObj.getAuthString(), commandObj.getGameID(), webSocketSessions);

        } else if (msg.getCommandType() == UserGameCommand.CommandType.JOIN_PLAYER) {
            JoinPlayer command = new Gson().fromJson(message, JoinPlayer.class);
            GameService.joinPlayer(command.getAuthString(), command.getGameID(), command.getPlayerColor(), webSocketSessions);

        } else if (msg.getCommandType() == UserGameCommand.CommandType.LEAVE) {
            Leave commandObj = new Gson().fromJson(message, Leave.class);
            GameService.leaveGame(commandObj.getAuthString(), commandObj.getGameID(), webSocketSessions);

        } else if (msg.getCommandType() == UserGameCommand.CommandType.RESIGN) {
            Resign command = new Gson().fromJson(message, Resign.class);
            GameService.resignGame(command.getAuthString(), command.getGameID(), webSocketSessions);

        }

    }


}
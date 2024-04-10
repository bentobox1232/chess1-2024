package webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.userCommands.*;


@WebSocket
public class WebSocketHandler {

    private final WebSocketSessions webSocketSessions = new WebSocketSessions();

    public WebSocketHandler() {}


    @OnWebSocketMessage
    public void onMessage(Session session, String message) {

        System.out.println("Message received from client: " + message);
        UserGameCommand msg = new Gson().fromJson(message, UserGameCommand.class);

        webSocketSessions.addSessionToGame(msg.getGameID(), msg.getAuthString(), session);


        if (msg.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){


        } else if (msg.getCommandType() == UserGameCommand.CommandType.JOIN_OBSERVER) {

        } else if (msg.getCommandType() == UserGameCommand.CommandType.JOIN_PLAYER) {
            JoinPlayer command = new Gson().fromJson(message, JoinPlayer.class);
            GameService.joinPlayer(command.getAuthString(), command.getGameID(), command.getPlayerColor(), webSocketSessions);

        } else if (msg.getCommandType() == UserGameCommand.CommandType.LEAVE) {

        } else if (msg.getCommandType() == UserGameCommand.CommandType.RESIGN) {

        }

    }


}
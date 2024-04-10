package server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

@WebSocket
public class WSServer {

    @OnWebSocketConnect
    public void onConnect(){

    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {

    }

    @OnWebSocketError
    public void onError() {

    }
    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        // Handle the incoming WebSocket message here
        // You can process the message and send a response back to the client
        System.out.println("Received message from client: " + message);
        try {
            session.getRemote().sendString("Server received message: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

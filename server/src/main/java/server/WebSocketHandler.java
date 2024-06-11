package server;
import org.eclipse.jetty.websocket.api.annotations.*;
import spark.Spark;
import org.eclipse.jetty.websocket.api.Session;

@WebSocket
public class WebSocketHandler {




    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println("Received message: " + message);
        try {
            session.getRemote().sendString(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

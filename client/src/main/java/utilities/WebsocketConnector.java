package utilities;

import com.google.gson.Gson;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;



@ClientEndpoint
public class WebsocketConnector extends Endpoint {
    private Session session;

    public WebsocketConnector(String hostname, int port) throws Exception {
        URI uri = new URI("ws://" + hostname + ":" + port + "/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                System.out.println(message);
            }
        });
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
    }

    public void send(String msg) throws IOException {
        this.session.getBasicRemote().sendText(msg);
    }
}



package utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import server.UserGameCommandDeserializer;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;




@ClientEndpoint
public class WebsocketConnector extends Endpoint {
    private Session session;

    public WebsocketConnector(String hostname, int port, ServerMessageObserver serverMessageObserver) throws Exception {
        URI uri = new URI("ws://" + hostname + ":" + port + "/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                GsonBuilder builder = new GsonBuilder();
                builder.registerTypeAdapter(ServerMessage.class, new ServerMessageDeserializer());
                Gson gson = builder.create();
                ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                serverMessageObserver.notify(serverMessage);
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



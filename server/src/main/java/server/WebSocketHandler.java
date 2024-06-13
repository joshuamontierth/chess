package server;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;
import service.UserGameCommandDeserializer;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.io.IOException;

import static service.WebSocketService.*;

@WebSocket
public class WebSocketHandler {


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UserGameCommand.class, new UserGameCommandDeserializer());
        Gson gson = builder.create();
        UserGameCommand gameCommand = gson.fromJson(message, UserGameCommand.class);


        switch (gameCommand.getCommandType()) {
            case CONNECT -> connectUser(gameCommand.getGameID(), session, gameCommand.getAuthString());
            case MAKE_MOVE -> makeMove(gameCommand.getGameID(), ((MakeMoveCommand) gameCommand).getMove(),session, gameCommand.getAuthString());
            case LEAVE -> leaveGame(gameCommand.getGameID(), session, gameCommand.getAuthString());
            case RESIGN -> resign(gameCommand.getGameID(), session, gameCommand.getAuthString());
        }

    }

}

package clientUtilities;

import com.google.gson.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.lang.reflect.Type;

public class ServerMessageDeserializer implements JsonDeserializer<ServerMessage> {
    @Override
    public ServerMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String typeString = jsonObject.get("type").getAsString();
        ServerMessage.ServerMessageType messageType = ServerMessage.ServerMessageType.valueOf(typeString);

        return switch(messageType) {
            case ERROR -> context.deserialize(jsonElement, ErrorMessage.class);
            case LOAD_GAME -> context.deserialize(jsonElement, LoadGameMessage.class);
            case NOTIFICATION -> context.deserialize(jsonElement, NotificationMessage.class);
        };
    }
}

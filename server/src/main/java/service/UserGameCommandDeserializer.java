package service;

import com.google.gson.*;
import websocket.commands.*;

import java.lang.reflect.Type;

public class UserGameCommandDeserializer implements JsonDeserializer<UserGameCommand> {
    @Override
    public UserGameCommand deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String typeString = jsonObject.get("type").getAsString();
        UserGameCommand.CommandType commandType = UserGameCommand.CommandType.valueOf(typeString);

        return switch(commandType) {
            case LEAVE -> context.deserialize(jsonElement, LeaveCommand.class);
            case RESIGN -> context.deserialize(jsonElement, ResignCommand.class);
            case CONNECT -> context.deserialize(jsonElement, ConnectCommand.class);
            case MAKE_MOVE -> context.deserialize(jsonElement, MakeMoveCommand.class);
        };
    }
}

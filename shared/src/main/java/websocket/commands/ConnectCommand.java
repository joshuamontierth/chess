package websocket.commands;

public class ConnectCommand extends UserGameCommand{
    public ConnectCommand(String authToken, int gameID) {
        super(authToken,CommandType.CONNECT,gameID);
    }
}

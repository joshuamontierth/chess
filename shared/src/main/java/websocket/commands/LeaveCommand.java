package websocket.commands;

public class LeaveCommand extends UserGameCommand{
    public LeaveCommand(String authToken, int gameID) {
        super(authToken,CommandType.LEAVE,gameID);
    }
}

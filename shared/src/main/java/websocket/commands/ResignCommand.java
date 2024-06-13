package websocket.commands;

public class ResignCommand extends UserGameCommand {
    public ResignCommand(String authToken,int gameID) {
        super(authToken,CommandType.RESIGN,gameID);
    }
}

package websocket.messages;

public class ErrorMessage extends ServerMessage{
    public ErrorMessage(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }
    private final String errorMessage;
    public String getErrorMessage() {
        return errorMessage;
    }

}

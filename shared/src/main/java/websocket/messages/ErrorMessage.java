package websocket.messages;

public class ErrorMessage extends ServerMessage{
    public ErrorMessage(String messageBody) {
        super(ServerMessageType.ERROR,messageBody);
    }
}

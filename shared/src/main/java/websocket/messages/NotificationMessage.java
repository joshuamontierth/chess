package websocket.messages;

public class NotificationMessage extends ServerMessage{
    public NotificationMessage(String messageBody) {
        super(ServerMessageType.NOTIFICATION, messageBody);
    }
}

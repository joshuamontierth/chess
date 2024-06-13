package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    public LoadGameMessage(String messageBody, ChessGame game) {
        super(ServerMessageType.LOAD_GAME,messageBody);
        this.game = game;
    }
    private final ChessGame game;
    public ChessGame getGame() {
        return game;
    }
}

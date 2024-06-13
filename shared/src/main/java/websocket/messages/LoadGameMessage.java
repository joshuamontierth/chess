package websocket.messages;

import chess.ChessGame;
import model.GameData;

public class LoadGameMessage extends ServerMessage {
    public LoadGameMessage(String messageBody, GameData game) {
        super(ServerMessageType.LOAD_GAME,messageBody);
        this.game = game;
    }
    private final GameData game;
    public GameData getGame() {
        return game;
    }
}

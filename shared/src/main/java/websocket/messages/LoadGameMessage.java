package websocket.messages;

import chess.ChessGame;
import model.GameData;

public class LoadGameMessage extends ServerMessage {
    public LoadGameMessage(GameData game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }
    private final GameData game;
    public GameData getGame() {
        return game;
    }
}

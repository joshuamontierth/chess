package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    private final ChessMove move;
    public MakeMoveCommand(String authToken,int gameID,ChessMove move) {
        super(authToken,CommandType.MAKE_MOVE,gameID);
        this.move = move;

    }

    public ChessMove getMove () {
        return move;
    }
}

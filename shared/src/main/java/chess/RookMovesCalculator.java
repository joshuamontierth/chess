package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator {
    @Override
    Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> out = new ArrayList<ChessMove>();
        moveHelper(board, position, 1,0,out,true);
        moveHelper(board, position, 0,1,out,true);
        moveHelper(board, position, 0,-1,out,true);
        moveHelper(board, position, -1,0,out,true);
        return out;
    }

}
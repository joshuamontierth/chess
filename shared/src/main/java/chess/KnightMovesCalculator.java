package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {
    @Override
    Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> out = new ArrayList<ChessMove>();
        moveHelper(board, position, 2,1,out,false);
        moveHelper(board, position, 2,-1,out,false);
        moveHelper(board, position, -2,1,out,false);
        moveHelper(board, position, -2,-1,out,false);
        moveHelper(board, position, 1,2,out,false);
        moveHelper(board, position, 1,-2,out,false);
        moveHelper(board, position, -1,2,out,false);
        moveHelper(board, position, -1,-2,out,false);
        return out;
    }
}
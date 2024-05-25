package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator {
    @Override
    Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> out = new ArrayList<ChessMove>();
        moveHelper(board, position, 1,1,out,true);
        moveHelper(board, position, 1,-1,out,true);
        moveHelper(board, position, -1,1,out,true);
        moveHelper(board, position, -1,-1,out,true);
        return out;
    }

}
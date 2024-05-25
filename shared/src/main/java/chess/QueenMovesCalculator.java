package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator {
    @Override
    Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> rookMoves = new RookMovesCalculator().calculateMoves(board,position);
        Collection<ChessMove> bishopMoves = new BishopMovesCalculator().calculateMoves(board,position);
        rookMoves.addAll(bishopMoves);
        return rookMoves;
    }


}
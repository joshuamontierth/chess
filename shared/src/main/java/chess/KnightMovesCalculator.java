package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator {
    @Override
    Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> out = new ArrayList<ChessMove>();
        moveHelper(board, position, 2,1,out);
        moveHelper(board, position, 2,-1,out);
        moveHelper(board, position, -2,1,out);
        moveHelper(board, position, -2,-1,out);
        moveHelper(board, position, 1,2,out);
        moveHelper(board, position, 1,-2,out);
        moveHelper(board, position, -1,2,out);
        moveHelper(board, position, -1,-2,out);
        return out;
    }

    void moveHelper(ChessBoard board, ChessPosition position, int x, int y, Collection<ChessMove> out) {
        ChessPosition currentPos = new ChessPosition(position.rowPos,position.colPos);
        currentPos.rowPos += x;
        currentPos.colPos += y;
        if(board.isValidSquare(currentPos)) {

            if (board.getPiece(currentPos) != null) {
                if (board.getPiece(currentPos).pieceColor != board.getPiece(position).pieceColor) {
                    out.add(new ChessMove(position, new ChessPosition(currentPos.rowPos,currentPos.colPos), null));
                }
                return;
            } else {
                out.add(new ChessMove(position, new ChessPosition(currentPos.rowPos,currentPos.colPos), null));
            }
        }
    }
}
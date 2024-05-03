package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator {
    @Override
    Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> out = new ArrayList<ChessMove>();
        moveHelper(board, position, 1,1,out);
        moveHelper(board, position, 1,-1,out);
        moveHelper(board, position, -1,1,out);
        moveHelper(board, position, -1,-1,out);
        moveHelper(board, position, 1,0,out);
        moveHelper(board, position, 0,1,out);
        moveHelper(board, position, 0,-1,out);
        moveHelper(board, position, -1,0,out);
        return out;
    }

    void moveHelper(ChessBoard board, ChessPosition position, int x, int y, Collection<ChessMove> out) {
        ChessPosition currentPos = new ChessPosition(position.rowPos,position.colPos);
        currentPos.rowPos += x;
        currentPos.colPos += y;
        while(currentPos.getRow() <= 8 && currentPos.getColumn() <= 8 && currentPos.getRow() >= 1 && currentPos.getColumn() >= 1) {

            if (board.getPiece(currentPos) != null) {
                if (board.getPiece(currentPos).pieceColor != board.getPiece(position).pieceColor) {
                    out.add(new ChessMove(position, new ChessPosition(currentPos.rowPos,currentPos.colPos += y), null));


                }
            return;
            } else {
                out.add(new ChessMove(position, new ChessPosition(currentPos.rowPos,currentPos.colPos += y), null));

                currentPos.rowPos += x;
                currentPos.colPos += y;


            }
        }
    }
}
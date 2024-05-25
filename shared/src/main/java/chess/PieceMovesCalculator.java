package chess;

import java.util.Collection;

public abstract class PieceMovesCalculator {
    abstract Collection<ChessMove>  calculateMoves(ChessBoard board, ChessPosition position);
    void moveHelper(ChessBoard board, ChessPosition position, int x, int y, Collection<ChessMove> out, boolean continuous) {
        ChessPosition currentPos = new ChessPosition(position.rowPos,position.colPos);
        currentPos.rowPos += x;
        currentPos.colPos += y;
        while(board.isValidSquare(currentPos)) {

            if (board.getPiece(currentPos) != null) {
                if (board.getPiece(currentPos).pieceColor != board.getPiece(position).pieceColor) {
                    out.add(new ChessMove(position, new ChessPosition(currentPos.rowPos,currentPos.colPos), null));
                }
                return;
            } else {
                out.add(new ChessMove(position, new ChessPosition(currentPos.rowPos,currentPos.colPos), null));
                currentPos.rowPos += x;
                currentPos.colPos += y;
            }
            if (!continuous) {
                break;
            }
        }


    }
}

package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {
    @Override
    Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> out = new ArrayList<ChessMove>();
        ChessGame.TeamColor team = board.getPiece(position).getTeamColor();
        int forward;
        if(team == ChessGame.TeamColor.WHITE) {
            forward = 1;
        }
        else {
            forward = -1;
        }
        ChessPosition forwardRight = new ChessPosition(position.rowPos + forward,position.colPos+1);
        ChessPosition forwardLeft = new ChessPosition(position.rowPos + forward,position.colPos-1);
        ChessPosition justForward = new ChessPosition(position.rowPos + forward,position.colPos);

        if (!board.isOccupied(justForward)) {
            out.add(new ChessMove(position, justForward,board.getPiece(position).promotionPiece));
        }
        if (board.isOccupied(forwardRight) && board.hasEnemy(team,forwardRight)) {
            out.add(new ChessMove(position, forwardRight,board.getPiece(position).promotionPiece));
        }
        if (board.isOccupied(forwardLeft) && board.hasEnemy(team,forwardLeft)) {
            out.add(new ChessMove(position, forwardLeft,board.getPiece(position).promotionPiece));
        }

    return out;
    }

}
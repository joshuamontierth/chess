package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator {
    @Override


    Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> out = new ArrayList<ChessMove>();
        ChessGame.TeamColor team = board.getPiece(position).getTeamColor();
        int forward;
        boolean startingPosition = false;
        if(team == ChessGame.TeamColor.WHITE) {
            forward = 1;
            if (position.rowPos == 2) {
                startingPosition = true;
            }
        }
        else {
            forward = -1;
            if (position.rowPos == 7) {
                startingPosition = true;
            }
        }
        ChessPosition forwardRight = new ChessPosition(position.rowPos + forward,position.colPos+1);
        ChessPosition forwardLeft = new ChessPosition(position.rowPos + forward,position.colPos-1);
        ChessPosition justForward = new ChessPosition(position.rowPos + forward,position.colPos);
        ChessPosition twoForward = new ChessPosition(position.rowPos + forward*2,position.colPos);

        //check if valid first, then just if occupied
        if (board.isValidSquare(justForward) && !board.isOccupied(justForward)) {
            boolean promotion = justForward.rowPos == 1 || justForward.rowPos == 8;
            promotionHelper(out,position,justForward,promotion);

        }
        if (board.isValidSquare(twoForward)&& !board.isOccupied(twoForward) && startingPosition &&!board.isOccupied(justForward)) {
            out.add(new ChessMove(position, twoForward,null));
        }

        if (board.isValidSquare(forwardRight)&&board.isOccupied(forwardRight) && board.hasEnemy(team,forwardRight)) {
            boolean promotion = forwardRight.rowPos == 1 || forwardRight.rowPos == 8;
            promotionHelper(out,position,forwardRight,promotion);
        }
        if (board.isValidSquare(forwardLeft)&& board.isOccupied(forwardLeft) && board.hasEnemy(team,forwardLeft)) {
            boolean promotion = forwardLeft.rowPos == 1 || forwardLeft.rowPos == 8;
            promotionHelper(out,position,forwardLeft,promotion);
        }

    return out;
    }
    void promotionHelper(Collection<ChessMove> out,ChessPosition oldPosition , ChessPosition newPosition, boolean promote) {
        if (promote) {
            out.add(new ChessMove(oldPosition, newPosition, ChessPiece.PieceType.QUEEN));
            out.add(new ChessMove(oldPosition, newPosition, ChessPiece.PieceType.ROOK));
            out.add(new ChessMove(oldPosition, newPosition, ChessPiece.PieceType.KNIGHT));
            out.add(new ChessMove(oldPosition, newPosition, ChessPiece.PieceType.BISHOP));

        }
        else {
            out.add(new ChessMove(oldPosition, newPosition, null));
        }
    }


}
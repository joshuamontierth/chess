package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard implements Cloneable {
    ChessPiece[][] board;


    public ChessBoard() {
        board = new ChessPiece[8][8];
    }
    @Override
    public ChessBoard clone() throws CloneNotSupportedException {
        super.clone();
        ChessBoard newBoard = new ChessBoard();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                newBoard.board[i][j] = this.board[i][j];
            }
        }
        return newBoard;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {

        board[position.rowPos-1][position.colPos-1] = piece;

    }
    public boolean isValidSquare(ChessPosition position) {
        return position.rowPos <= 8 && position.colPos <= 8 && position.rowPos >= 1 && position.colPos >= 1;
    }
    public boolean isOccupied(ChessPosition position) {
        return board[position.rowPos-1][position.colPos-1] != null;
    }
    public boolean hasEnemy(ChessGame.TeamColor team, ChessPosition position) {
        return board[position.rowPos-1][position.colPos-1].pieceColor != team;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        if (position == null) {
            return null;
        }
        if (board[position.rowPos-1][position.colPos-1] != null) {
            return board[position.rowPos-1][position.colPos-1];
        }
        return null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        //set pawns
        for (int i = 0; i < 8; i++) {
            addPiece(new ChessPosition(2,i+1),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7,i+1),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));

        }

        // Rooks
        addPiece(new ChessPosition(1,1),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1,8),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8,1),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8,8),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));

        // Knights
        addPiece(new ChessPosition(1,2),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1,7),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8,2),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8,7),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));

        // Bishops
        addPiece(new ChessPosition(1,3),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1,6),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8,3),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8,6),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));

        // Queens
        addPiece(new ChessPosition(1,4),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(8,4),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));

        // Kings
        addPiece(new ChessPosition(1,5),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8,5),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
    }

    public ChessPiece[][] getBoard() {
        return board;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    sb.append(board[i][j].toString());
                }
                else {
                    sb.append("    ");

                }
                sb.append(" | ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}

package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board;
    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if(this.board.getPiece(startPosition) != null) {
            Collection<ChessMove> allPieceMoves = board.getPiece(startPosition).pieceMoves(this.board,startPosition);
            for (ChessMove move : allPieceMoves) {

            }
        }
        else {
            return null;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        Collection<ChessMove> validMoves = validMoves(startPosition);

        if (validMoves.contains(move)) {
            board.addPiece(endPosition,board.getPiece(startPosition));
            board.addPiece(startPosition,null);
            nextTurn();
            board.getPiece(startPosition).hasMoved = true;

        }
        else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king = findKing(teamColor);
        Collection<ChessMove> allEnemyMoves = findAllTeamMoves(oppositeTeam(teamColor));
        for (ChessMove move : allEnemyMoves) {
            ChessPosition endPosition = move.getEndPosition();
            if (endPosition.equals(king)) {
                return true;
            }
        }
        return false;


    }



    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        findAllTeamMoves()
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> allTeamMoves = findAllTeamMoves(teamColor);
        return (allTeamMoves.isEmpty() && !isInCheck(teamColor));
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    public Collection<ChessPosition> getAllTeamPieces(TeamColor team) {
        Collection<ChessPosition> out = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = this.board.getPiece(currentPosition);
                if (currentPiece != null && currentPiece.getTeamColor() == team) {
                    out.add(currentPosition);
                }
            }
        }
        return out;
    }
    public ChessPosition findKing(TeamColor team) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                ChessPiece currentPiece = this.board.getPiece(currentPosition);
                if (currentPiece != null && currentPiece.getTeamColor() == team && currentPiece.getPieceType() == ChessPiece.PieceType.KING) {
                    return currentPosition;
                }
            }
        }
        return null;
    }

    public TeamColor oppositeTeam(TeamColor team) {
        if (team == TeamColor.BLACK) {
            return TeamColor.WHITE;
        }
        else {
            return TeamColor.BLACK;
        }
    }

    public Collection<ChessMove> findAllTeamMoves(TeamColor team) {
       Collection<ChessMove> out = new ArrayList<>();
       Collection<ChessPosition> allPieces = getAllTeamPieces(team);
        for (ChessPosition piece : allPieces) {
            Collection<ChessMove> moves = validMoves(piece);
            out.addAll(moves);
        }
        return out;

    }

    public void nextTurn() {
        this.teamTurn = oppositeTeam(this.teamTurn);
    }

    public Collection<ChessMove> castle(TeamColor team) {
        // return empty collection if castling is not possible
        boolean possible = !board.getPiece(findKing(team)).hasMoved;
        ChessPiece LeftRook;
        ChessPiece RightRook;
        if (team == TeamColor.WHITE) {
             LeftRook = board.getPiece(new ChessPosition(1,1));
             RightRook = board.getPiece(new ChessPosition(1,8));
        }
        else {
            LeftRook = board.getPiece(new ChessPosition(8,1));
            RightRook = board.getPiece(new ChessPosition(8,8));
        }
        if ((LeftRook != null && LeftRook.hasMoved) || RightRook != null && RightRook.hasMoved) {
            possible = false;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }
}

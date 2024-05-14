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
    private ChessMove previousMove;
    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        previousMove = null;
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
            ChessGame.TeamColor pieceTeam = board.getPiece(startPosition).getTeamColor();
            Collection<ChessMove> allPieceMoves = board.getPiece(startPosition).pieceMoves(this.board,startPosition);
            ChessBoard originalBoard = this.board;
            Collection<ChessMove> validMoves = new ArrayList<>();
            for (ChessMove move : allPieceMoves) {
                try {
                    board = board.clone();
                    board.addPiece(move.getEndPosition(),board.getPiece(startPosition));
                    board.addPiece(startPosition,null);
                    if (!isInCheck(pieceTeam)) {
                        validMoves.add(move);
                    }
                    board = originalBoard;

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            if (startPosition.equals(findKing(pieceTeam))) {
                Collection<ChessMove> castlingMoves = castling(pieceTeam);
                for (ChessMove move : castlingMoves) {
                    if (move.getStartPosition().equals(startPosition)) {

                        validMoves.add(move);
                    }
                }
            }
            ChessMove enPassant = enPassant(startPosition);
            if (enPassant != null) {
                validMoves.add(enPassant);
            }
            return validMoves;
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
        if (this.board.getPiece(startPosition) == null) {

            throw new InvalidMoveException();
        }
        if (this.board.getPiece(startPosition).getTeamColor() != this.teamTurn) {

            throw new InvalidMoveException();
        }
        ChessPosition endPosition = move.getEndPosition();
        Collection<ChessMove> validMoves = validMoves(startPosition);

        if (validMoves.contains(move)) {
            executeEnPassant(move);
            if (move.getPromotionPiece() == null) {
                board.addPiece(endPosition, board.getPiece(startPosition));
            }
            else {
                board.addPiece(endPosition, new ChessPiece(teamTurn,move.getPromotionPiece()));
            }

            board.getPiece(startPosition).hasMoved = true;
            executeCastling(move);
            board.addPiece(startPosition,null);

            nextTurn();

            previousMove = move;
        }

        else {

            throw new InvalidMoveException();
        }
    }
    public void executeEnPassant(ChessMove move) {
        if (previousMove == null) {
            return;
        }
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        int startCol = startPosition.getColumn();
        int endCol = endPosition.getColumn();

        if (board.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN && startCol != endCol && board.getPiece(endPosition) == null) {
            board.addPiece(new ChessPosition(startPosition.getRow(),endCol),null);
        }
    }
    public void executeCastling(ChessMove move) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        int row = startPosition.getRow();

        if (board.getPiece(startPosition) != null && board.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.KING) {
            if ((startPosition.getColumn()- endPosition.getColumn()) == 2) {
                ChessPosition rookStart = new ChessPosition(row,1);
                ChessPosition rookEnd = new ChessPosition(row,4);
                board.addPiece(rookEnd,board.getPiece(rookStart));
                board.getPiece(rookStart).hasMoved = true;
                board.addPiece(rookStart,null);
            }
            else if ((endPosition.getColumn() - startPosition.getColumn()) == 2) {
                ChessPosition rookStart = new ChessPosition(row,8);
                ChessPosition rookEnd = new ChessPosition(row,6);
                board.addPiece(rookEnd,board.getPiece(rookStart));
                board.getPiece(rookStart).hasMoved = true;
                board.addPiece(rookStart,null);
            }
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
        return isSquareInCheck(king,teamColor);


    }

    public boolean isSquareInCheck(ChessPosition square, TeamColor teamColor) {

        Collection<ChessMove> allEnemyMoves = findAllTeamMoves(oppositeTeam(teamColor));
        for (ChessMove move : allEnemyMoves) {
            ChessPosition endPosition = move.getEndPosition();
            if (endPosition.equals(square)) {
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
        Collection<ChessMove> allTeamMoves = findAllTeamMoves(teamColor);
        ChessBoard originalBoard = this.board;
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (ChessMove move : allTeamMoves) {
            try {
                board = board.clone();
                board.addPiece(move.getEndPosition(),board.getPiece(move.getStartPosition()));
                board.addPiece(move.getStartPosition(),null);
                if (!isInCheck(teamColor)) {
                    validMoves.add(move);
                }
                board = originalBoard;

            } catch (Exception e) {

                throw new RuntimeException(e);
            }

        }
        return validMoves.isEmpty();

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessPosition> allPieces = getAllTeamPieces(teamColor);
        Collection<ChessMove> allValidMoves = new ArrayList<>();
        for (ChessPosition position : allPieces) {
            allValidMoves.addAll(validMoves(position));
        }
        return (allValidMoves.isEmpty() && !isInCheck(teamColor));
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
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
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
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
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
            Collection<ChessMove> moves = board.getPiece(piece).pieceMoves(board,piece);
            out.addAll(moves);
        }
        return out;

    }

    public void nextTurn() {
        this.teamTurn = oppositeTeam(this.teamTurn);
    }

    public Collection<ChessMove> castling(TeamColor team) {
        ChessPosition kingSquare = findKing(team);
        Collection<ChessMove> moves = new ArrayList<>();
        if (isInCheck(team)) {
            return moves;
        }


        ChessPiece king = board.getPiece(kingSquare);
        boolean leftCastle = true;
        boolean rightCastle = true;

        int row = 1;
        if (team == TeamColor.BLACK) {
            row = 8;
        }
        ChessPosition leftRookSquare = new ChessPosition(row,1);
        ChessPosition rightRookSquare = new ChessPosition(row,8);
        ChessPiece leftRook = board.getPiece(leftRookSquare);
        ChessPiece rightRook = board.getPiece(rightRookSquare);

        if (king == null || king.hasMoved) {
            return moves;
        }

        if (leftRook == null || leftRook.getPieceType() != ChessPiece.PieceType.ROOK || leftRook.hasMoved) {
            leftCastle = false;
        }
        if (rightRook == null || rightRook.getPieceType() != ChessPiece.PieceType.ROOK || rightRook.hasMoved) {
            rightCastle = false;
        }


        //check in-between squares
        for (int col = 2; col <= 4; col++) {
            ChessPosition currentPos = new ChessPosition(row, col);
            if (board.getPiece(currentPos) != null || isSquareInCheck(currentPos,team)) {
                leftCastle = false;
                break;
            }
        }

        for (int col = 6; col <= 7; col++) {
            ChessPosition currentPos = new ChessPosition(row, col);
            if (board.getPiece(currentPos) != null || isSquareInCheck(currentPos,team)) {
                rightCastle = false;
                break;
            }
        }
        if (leftCastle) {
            ChessMove queenSideCastle = new ChessMove(kingSquare,new ChessPosition(row,3),null);
            moves.add(queenSideCastle);
        }
        if (rightCastle) {
            ChessMove kingSideCastle = new ChessMove(kingSquare,new ChessPosition(row,7),null);
            moves.add(kingSideCastle);
        }
        return moves;

    }

    public ChessMove enPassant(ChessPosition pawnPosition) {
        if (previousMove == null) {
            return null;
        }
        if (board.getPiece(pawnPosition).getPieceType() != ChessPiece.PieceType.PAWN) {
            return null;
        }
        if(board.getPiece(previousMove.getEndPosition()).getPieceType() != ChessPiece.PieceType.PAWN) {
            return null;
        }
        if(Math.abs(previousMove.getEndPosition().getRow() - previousMove.getStartPosition().getRow()) != 2) {
            return null;
        }
        int pawnCol = pawnPosition.getColumn();
        int pawnRow = pawnPosition.getRow();
        if (Math.abs(previousMove.getEndPosition().getColumn() - pawnCol) != 1) {
            return null;
        }
        if (pawnRow != previousMove.getEndPosition().getRow()) {
            return null;
        }
        TeamColor team = board.getPiece(pawnPosition).getTeamColor();
        int forward = 1;
        if (team == TeamColor.BLACK) {
            forward = -1;
        }
        return new ChessMove(pawnPosition,new ChessPosition(pawnRow + forward,previousMove.endPosition.getColumn()),null);
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

package ui;

import chess.*;

public class DrawBoard {
    String[][] board;
    public DrawBoard(ChessPiece.PieceType[][] board) {
        this.board = convertBoardToString(board);
    }
    private String[][] convertBoardToString(ChessPiece.PieceType[][] board) {
        String[][] out = new String[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                 out[i][j] = switch (board[i][j]) {
                    case KING -> "K";
                    case QUEEN -> "Q";
                    case BISHOP -> "B";
                    case KNIGHT -> "N";
                    case ROOK -> "R";
                    case PAWN -> "P";
                };
            }
        }
        return out;
    }
}

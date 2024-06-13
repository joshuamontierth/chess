package ui;

import chess.*;

public class BoardDrawer {
    private final ChessPiece[][] board;
    public BoardDrawer(ChessPiece[][] board) {
        this.board = board;
    }
    private String convertToChar(ChessPiece.PieceType type) {
        return switch (type) {
            case KING -> "K";
            case QUEEN -> "Q";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case ROOK -> "R";
            case PAWN -> "P";
        };
    }
    private void drawSquare(String squareColor, ChessPiece piece) {
        String textColor = EscapeSequences.SET_TEXT_COLOR_BLUE;
        if (squareColor.equals("W")) {
            squareColor = EscapeSequences.SET_BG_COLOR_WHITE;
        }
        else {
            squareColor = EscapeSequences.SET_BG_COLOR_BLACK;
        }
        String pieceChar = " ";
        if (piece != null) {
            pieceChar = convertToChar(piece.getPieceType());
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                textColor = EscapeSequences.SET_TEXT_COLOR_RED;
            }
        }

        System.out.print(squareColor + " " + textColor + pieceChar + " ");
    }

    private void drawLine(int lineNumber, boolean whiteOrientation) {
        String squareColor = whiteOrientation ^ ((lineNumber+1) % 2 == 0) ? "B" : "W";
        System.out.print(EscapeSequences.SET_BG_COLOR_BLUE+ EscapeSequences.SET_TEXT_COLOR_BLACK + " " + (lineNumber+1) + " ");

        if (whiteOrientation) {
            for (int i = 0; i < 8; i++) {
                squareColor = drawLineLoop(lineNumber, squareColor, i);
            }
        }
        else {
            for (int i = 7; i >= 0; i--) {
                squareColor = drawLineLoop(lineNumber, squareColor, i);
            }
        }
        System.out.print(EscapeSequences.SET_BG_COLOR_BLUE+ EscapeSequences.SET_TEXT_COLOR_BLACK + " " + (lineNumber+1) + " " + EscapeSequences.RESET_BG_COLOR + "\n");


    }
    private String drawLineLoop(int lineNumber, String squareColor, int i) {
        drawSquare(squareColor,board[lineNumber][i]);
        if (squareColor.equals("B")) {
            squareColor = "W";
        }
        else {
            squareColor = "B";
        }
        return squareColor;
    }
    private void drawHorizontalBoarder(boolean whiteOrientation) {
        char currentChar = 'a';
        if (!whiteOrientation) {
            currentChar = 'h';
        }
        System.out.print(EscapeSequences.SET_TEXT_BOLD);
        System.out.print(EscapeSequences.SET_BG_COLOR_BLUE+ EscapeSequences.SET_TEXT_COLOR_BLACK + "   ");

        for (int i = 0; i < 8; i++) {
            System.out.print(" " + currentChar + " ");
            if (whiteOrientation) {
                currentChar = (char) ( currentChar + 1);
            }
            else {
                currentChar = (char) ( currentChar - 1);
            }
        }
        System.out.print("   "+ EscapeSequences.RESET_BG_COLOR + "\n");


    }
    public void drawBoard(boolean whiteOrientation) {
        drawHorizontalBoarder(whiteOrientation);

        if (whiteOrientation) {
            for (int i = 7; i >= 0; i--) {
                drawLine(i,true);
            }
        }
        else {
            for (int i = 0; i < 8; i++) {
                drawLine(i,false);
            }
        }
        drawHorizontalBoarder(whiteOrientation);
        System.out.print(EscapeSequences.RESET_TEXT_COLOR + EscapeSequences.SET_TEXT_FAINT);
    }


}

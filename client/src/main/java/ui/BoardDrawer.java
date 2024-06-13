package ui;

import chess.*;
import org.glassfish.grizzly.utils.Pair;

import java.util.ArrayList;
import java.util.Collection;

public class BoardDrawer {
    private final ChessPiece[][] board;
    private BoardHighlighter boardHighlighter;
    public BoardDrawer(ChessPiece[][] board) {
        this.board = board;
        boardHighlighter = null;
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

        switch (squareColor) {
            case "W" -> squareColor = EscapeSequences.SET_BG_COLOR_WHITE;
            case "G" -> squareColor = EscapeSequences.SET_BG_COLOR_GREEN;
            case "DG" -> squareColor = EscapeSequences.SET_BG_COLOR_DARK_GREEN;
            case "Y" -> squareColor = EscapeSequences.SET_BG_COLOR_YELLOW;
            default -> squareColor = EscapeSequences.SET_BG_COLOR_BLACK;
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
        String previousSquareColor = squareColor;
        if (boardHighlighter != null) {
            if (boardHighlighter.getRoot().getFirst() == lineNumber && boardHighlighter.getRoot().getSecond() == i) {
                squareColor = "Y";
            }
            for (var square : boardHighlighter.getSquares()) {
                if (square.getFirst() == lineNumber && square.getSecond() == i) {
                    if (squareColor.equals("B")) {
                        squareColor = "DG";
                    }
                    else {
                        squareColor = "G";
                    }
                    break;
                }
            }
        }
        drawSquare(squareColor,board[lineNumber][i]);
        if (previousSquareColor.equals("B")) {
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
    public void drawBoardValidSquares(ChessGame game, ChessPosition position, boolean whiteOrientation) {
        var validMoves = game.validMoves(position);
        if (validMoves== null) {
            System.out.println("No valid moves there!");
            return;
        }
        if (game.getBoard().getPiece(position).getTeamColor() != game.getTeamTurn()) {
            System.out.println("It is not their turn, no valid moves.");
            return;
        }
        boardHighlighter = new BoardHighlighter(validMoves,position);
        drawBoard(whiteOrientation);


    }
    private class BoardHighlighter {
        ArrayList<Pair<Integer,Integer>> squares;
        Pair<Integer,Integer> root;
        public BoardHighlighter(Collection<ChessMove> validMoves, ChessPosition rootPosition) {

            this.root = new Pair<>(rootPosition.getRow()-1, rootPosition.getColumn()-1);
            squares = new ArrayList<>();
            for (var move : validMoves) {
                squares.add(new Pair<>(move.getEndPosition().getRow()-1, move.getEndPosition().getColumn()-1));
            }
        }
        public ArrayList<Pair<Integer,Integer>> getSquares() {
            return squares;
        }
        public Pair<Integer,Integer> getRoot() {
            return root;
        }
    }


}

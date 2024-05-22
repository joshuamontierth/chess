package model;

import chess.ChessBoard;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessBoard board) {
}

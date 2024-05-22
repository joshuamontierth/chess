package service;

import chess.ChessBoard;
import dataaccess.GameDAOInterface;
import dataaccess.MemoryGameDAO;
import model.GameData;
import utilities.CreateGameRequest;
import utilities.CreateGameResult;

import java.util.ArrayList;

public class CreateGameService {
    public CreateGameResult createGame(CreateGameRequest req) {
        GameDAOInterface gameDAO = new MemoryGameDAO();
        ArrayList<GameData> games = gameDAO.listGames();
        int prevGameID = games.getLast().gameID();
        GameData newGame = new GameData(prevGameID++,null,null,req.gameName(),new ChessBoard())
    }
}

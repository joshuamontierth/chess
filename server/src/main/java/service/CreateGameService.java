package service;

import chess.ChessBoard;
import model.GameData;
import dataaccess.*;
import utilities.*;

import java.util.ArrayList;

public class CreateGameService extends Service {
    public CreateGameResult createGame(CreateGameRequest req) throws HTMLException {
        verifyUser(req.authToken());
        GameDAOInterface gameDAO = new MemoryGameDAO();
        ArrayList<GameData> games = gameDAO.listGames();
        int prevGameID = games.getLast().gameID();
        int newGameID = prevGameID + 1;
        GameData newGame = new GameData(newGameID,null,null,req.gameName(),new ChessBoard());
        gameDAO.createGame(newGame);
        return new CreateGameResult(newGameID);
    }
}

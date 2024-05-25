package service;

import chess.ChessGame;
import model.GameData;
import dataaccess.*;
import utilities.*;

import java.util.ArrayList;

public class CreateGameService extends Service {
    public static CreateGameResult createGame(CreateGameRequest req) throws HTMLException {
        verifyUser(req.authToken());
        if (req.gameName() == null || req.gameName().isEmpty()) {
            throw new HTMLException("Error: Fill out all required fields", 400);
        }
        GameDAOInterface gameDAO = new MemoryGameDAO();
        ArrayList<GameData> games = gameDAO.listGames();
        int newGameID = 1;
        if (!games.isEmpty()) {
            int prevGameID = games.getLast().gameID();
            newGameID = prevGameID + 1;
        }

        GameData newGame = new GameData(newGameID,null,null,req.gameName(),new ChessGame());
        gameDAO.createGame(newGame);
        return new CreateGameResult(newGameID);
    }
}

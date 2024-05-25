package service;

import chess.ChessBoard;
import model.GameData;
import dataaccess.*;
import utilities.*;

import java.util.ArrayList;

public class CreateGameService extends Service {
    public static CreateGameResult createGame(CreateGameRequest req) throws HTMLException {
        verifyUser(req.authToken());
        GameDAOInterface gameDAO = new MemoryGameDAO();
        ArrayList<GameData> games = gameDAO.listGames();
        int newGameID = 1;
        if (!games.isEmpty()) {
            int prevGameID = games.getLast().gameID();
            newGameID = prevGameID + 1;
        }

        GameData newGame = new GameData(newGameID,null,null,req.gameName(),new ChessBoard());
        gameDAO.createGame(newGame);
        System.out.println(newGameID);
        return new CreateGameResult(newGameID);
    }
}

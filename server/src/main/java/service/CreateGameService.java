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
        GameDAOInterface gameDAO = new MySQLGameDAO();
        GameData newGame = new GameData(0,null,null,req.gameName(),new ChessGame());
        int newGameID = gameDAO.createGame(newGame);
        return new CreateGameResult(newGameID);
    }
}

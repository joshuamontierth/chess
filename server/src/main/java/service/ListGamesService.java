package service;

import dataaccess.*;
import model.GameData;
import utilities.*;

import java.util.Collection;

public class ListGamesService extends Service{
    public static ListGamesResult listGames(ListGamesRequest req) throws HTMLException {
        verifyUser(req.authToken());
        GameDAOInterface gameDAO = new MemoryGameDAO();
        Collection<GameData> gameList = gameDAO.listGames();
        return new ListGamesResult(gameList);
    }
}

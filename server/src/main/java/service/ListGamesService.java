package service;

import dataaccess.*;
import model.GameData;
import utilities.*;
import utilities.request.ListGamesRequest;
import utilities.result.ListGamesResult;

import java.util.Collection;

public class ListGamesService extends Service{
    public static ListGamesResult listGames(ListGamesRequest req) throws HTMLException {
        verifyUser(req.authToken());
        GameDAOInterface gameDAO = new MySQLGameDAO();
        Collection<GameData> gameList = gameDAO.listGames();
        return new ListGamesResult(gameList);
    }
}

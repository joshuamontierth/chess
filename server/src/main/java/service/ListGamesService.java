package service;

import dataaccess.*;
import model.GameData;
import utilities.*;

import java.util.ArrayList;

public class ListGamesService extends Service{
    public ListGamesResult listGames(ListGamesRequest req) throws HTMLException {
        verifyUser(req.authToken());
        GameDAOInterface gameDAO = new MemoryGameDAO();
        ArrayList<GameData> gameList = gameDAO.listGames();
        return new ListGamesResult(gameList);
    }
}

package dataaccess;

import model.GameData;


import java.util.ArrayList;
import java.util.TreeMap;

public class MemoryGameDAO implements GameDAOInterface{
    static TreeMap<Integer, GameData> games = new TreeMap<>();
    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public void createGame(GameData game) {
        games.put(game.gameID(),game);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        GameData game = games.get(gameID);
        if (game == null) {
            throw new DataAccessException("Game does not exist");
        }
        return game;
    }

    @Override
    public ArrayList<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {
        if (games.containsKey(game.gameID())) {
            games.put(game.gameID(), game);
        }
        else {
            throw new DataAccessException("Game does not exist");
        }
    }
    public int getSize() {
        return games.size();
    }
}

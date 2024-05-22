package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAOInterface{
    HashMap<Integer, GameData> games;
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
        return (ArrayList<GameData>) games.values();
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
}

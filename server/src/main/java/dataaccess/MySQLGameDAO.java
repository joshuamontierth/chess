package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class MySQLGameDAO implements GameDAOInterface{
    @Override
    public void clear() {

    }

    @Override
    public void createGame(GameData game) {

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() {
        return null;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }
}

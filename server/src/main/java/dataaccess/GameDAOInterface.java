package dataaccess;

import model.GameData;

import java.util.ArrayList;


public interface GameDAOInterface {
    void clear();
    void createGame(GameData game);
    GameData getGame(int gameID) throws DataAccessException;
    ArrayList<GameData> listGames();
    void updateGame(GameData game) throws DataAccessException;
}

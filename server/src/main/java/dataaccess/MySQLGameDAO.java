package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MySQLGameDAO implements GameDAOInterface{
    MySQLGameDAO() {
        try {
            DatabaseManager.createDatabase();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        try (Connection c = DatabaseManager.getConnection()) {
            try (var preparedStatement = c.prepareStatement("DELETE FROM games;")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int createGame(GameData game) {
        try (Connection c = DatabaseManager.getConnection()) {
            try (var preparedStatement = c.prepareStatement("INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1,game.whiteUsername());
                preparedStatement.setString(2,game.blackUsername());
                preparedStatement.setString(3,game.gameName());
                preparedStatement.setString(4,serializeGame(game.game()));
                preparedStatement.executeUpdate();
                var rs = preparedStatement.getGeneratedKeys();
                rs.next();
                return rs.getInt(1);
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection c = DatabaseManager.getConnection()) {
            try (var preparedStatement = c.prepareStatement("SELECT * FROM games WHERE gameID = ?;")) {
                preparedStatement.setInt(1,gameID);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return new GameData(rs.getInt(1), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"),deserializeGame(rs.getString("game")));
                }
                else {
                    throw new DataAccessException("Game does not exist");
                }
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<GameData> listGames() {
        ArrayList<GameData> games = new ArrayList<>();
        try (Connection c = DatabaseManager.getConnection()) {
            try (var preparedStatement = c.prepareStatement("SELECT * FROM games;")) {
                var rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    games.add(new GameData(rs.getInt(1), rs.getString("whiteUsername"), rs.getString("blackUsername"), rs.getString("gameName"),deserializeGame(rs.getString("game"))));
                }
                return games;
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

        try (Connection c = DatabaseManager.getConnection()) {
            try (var preparedStatement = c.prepareStatement("UPDATE games SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?;")) {
                preparedStatement.setString(1,game.whiteUsername());
                preparedStatement.setString(2,game.blackUsername());
                preparedStatement.setString(3,game.gameName());
                preparedStatement.setString(4,serializeGame(game.game()));
                preparedStatement.setInt(5,game.gameID());
                int updated = preparedStatement.executeUpdate();

                if(updated != 1){
                    throw new DataAccessException("Game does not exist");
                }
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }


    }
    private String serializeGame(ChessGame game) {
        Gson gson = new Gson();
        return gson.toJson(game);
    }
    private ChessGame deserializeGame(String jsonGame) {
        Gson gson = new Gson();
        return gson.fromJson(jsonGame,ChessGame.class);
    }
}

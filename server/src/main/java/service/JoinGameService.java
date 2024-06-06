package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAOInterface;
import dataaccess.MySQLGameDAO;
import model.GameData;
import utilities.HTMLException;
import utilities.JoinGameRequest;
import utilities.JoinGameResult;

public class JoinGameService extends Service {
    public static JoinGameResult joinGame(JoinGameRequest req) throws HTMLException {
        String username = verifyUser(req.authToken());
        GameDAOInterface gameDAO = new MySQLGameDAO();
        GameData game;
        if(req.playerColor() == null || req.gameID() == null) {
            throw new HTMLException("Error: Fill out all required fields", 400);
        }
        try {
            game = gameDAO.getGame(req.gameID());
        } catch (DataAccessException e) {
            throw new HTMLException(e.getMessage(),400);
        }
        GameData updatedGame = updateUser(req,game,username);
        try {
            gameDAO.updateGame(updatedGame);
        } catch (DataAccessException e) {
            throw new HTMLException(e.getMessage(),500);
        }
        return new JoinGameResult();



    }
    private static GameData updateUser(JoinGameRequest req, GameData game, String username) throws HTMLException {
        String playerColorUsername;




        if (req.playerColor().equals("WHITE")) {
            playerColorUsername = game.whiteUsername();
        }
        else if (req.playerColor().equals("BLACK")) {
            playerColorUsername = game.blackUsername();
        }

        else if (req.playerColor().isEmpty()) {
            playerColorUsername = null;
        }
        else {
            throw new HTMLException("Error: Not a valid color", 400);
        }
        if (playerColorUsername != null) {
            throw new HTMLException("Error: already taken", 403);
        }
        GameData updatedGame;
        if (req.playerColor().equals("WHITE")) {
            updatedGame = new GameData(game.gameID(),username,game.blackUsername(), game.gameName(), game.game());
        }
        else if (req.playerColor().equals("BLACK")) {
            updatedGame = new GameData(game.gameID(),game.whiteUsername(),username, game.gameName(), game.game());

        }
        else {
            updatedGame = game;
        }
        return updatedGame;

    }

}

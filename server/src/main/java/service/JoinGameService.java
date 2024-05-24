package service;

import dataaccess.DataAccessException;
import dataaccess.GameDAOInterface;
import dataaccess.MemoryGameDAO;
import model.GameData;
import utilities.HTMLException;
import utilities.JoinGameRequest;
import utilities.JoinGameResult;

public class JoinGameService extends Service {
    public JoinGameResult joinGame(JoinGameRequest req) throws HTMLException {
        String username = verifyUser(req.AuthToken());
        GameDAOInterface gameDAO = new MemoryGameDAO();
        GameData game;
        try {
            game = gameDAO.getGame(req.gameID());
        } catch (DataAccessException e) {
            throw new HTMLException(e.getMessage(),500);
        }
        GameData updatedGame = updateUser(req,game,username);
        try {
            gameDAO.updateGame(updatedGame);
        } catch (DataAccessException e) {
            throw new HTMLException(e.getMessage(),500);
        }
        return new JoinGameResult();



    }
    private GameData updateUser(JoinGameRequest req, GameData game, String username) throws HTMLException {
        String playerColorUsername;
        if (req.playerColor().equals("WHITE")) {
            playerColorUsername = game.whiteUsername();
        }
        else if (req.playerColor().equals("BLACK")) {
            playerColorUsername = game.blackUsername();
        }
        else {
            throw new HTMLException("Error: Not a valid color", 500);
        }
        if (playerColorUsername != null) {
            throw new HTMLException("Error: already taken", 403);
        }
        GameData updatedGame;
        if (req.playerColor().equals("WHITE")) {
            updatedGame = new GameData(game.gameID(),username,game.blackUsername(), game.gameName(), game.board());
        }
        else {
            updatedGame = new GameData(game.gameID(),game.whiteUsername(),username, game.gameName(), game.board());

        }
        return updatedGame;

    }

}

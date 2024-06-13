package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MySQLAuthDAO;
import dataaccess.MySQLGameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class WebSocketService {
    private static final HashMap<Session, Integer> sessions = new HashMap<>();


    public static void resign(int gameID, Session session, String authString) throws IOException {
        String username = getUsername(authString,session);
        GameData game = getGameData(gameID, session);
        if (username == null || game == null) {
            return;
        }
        game.game().setGameComplete();
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        try {
            gameDAO.updateGame(game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        String color = username.equals(game.blackUsername()) ? "White" : "Black";
        ServerMessage resignBroadcastMessage = new NotificationMessage(username + " has resigned." + color + " has won!");
        broadcast(gameID,session,resignBroadcastMessage);

        ServerMessage resignMessage = new NotificationMessage("You have resigned.");
        send(session,resignMessage);
    }

    public static  void leaveGame(int gameID, Session session, String authString) throws IOException {
        String username = getUsername(authString,session);
        GameData game = getGameData(gameID, session);
        if (username == null || game == null) {
            return;
        }
        GameData newGame;
        if (game.blackUsername().equals(username)) {
            newGame = new GameData(gameID, game.whiteUsername(), null,game.gameName(),game.game());
        }
        else if(game.whiteUsername().equals(username)) {
            newGame = new GameData(gameID, null, game.blackUsername(),game.gameName(),game.game());
        }
        else {
            newGame = game;
        }
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        try {
            gameDAO.updateGame(newGame);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        ServerMessage leftBroadcastMessage = new NotificationMessage(username + " has left the game.");
        broadcast(gameID,session,leftBroadcastMessage);
        sessions.remove(session);

        ServerMessage leftMessage = new NotificationMessage("Successfully left the game");
        send(session,leftMessage);
    }

    public static  void makeMove(int gameID, ChessMove move, Session session, String authString) throws IOException {
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        GameData gameData = getGameData(gameID,session);
        String username = getUsername(authString,session);
        if (username == null || gameData == null) {
            return;
        }
        ChessGame game = gameData.game();

        try {
            game.makeMove(move);
        } catch (InvalidMoveException e) {
            sendError(session,"Error, invalid move");
            return;
        }
        GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
        try {
            gameDAO.updateGame(newGameData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        ServerMessage madeMoveMessageBroadcast = new LoadGameMessage(username + " has moved " + move,newGameData);
        broadcast(gameID,session,madeMoveMessageBroadcast);

        ServerMessage madeMoveMessage = new LoadGameMessage(null, newGameData);
        send(session,madeMoveMessage);
    }

    public static  void connectUser(int gameID, Session session,String authString) throws IOException {

        sessions.put(session,gameID);
        String username;
        GameData game = getGameData(gameID, session);

        username = getUsername(authString,session);
        if (username == null || game == null) {
            return;
        }
        String team = username.equals(game.blackUsername()) ? "Black" : "White";


        ServerMessage joinMessageBroadcast = new NotificationMessage(username + " has joined the game as " + team);
        broadcast(gameID,session,joinMessageBroadcast);

        ServerMessage joinMessage = new LoadGameMessage("You have successfully joined the game",game);
        send(session, joinMessage);
    }

    private static String getUsername(String authToken, Session session) throws IOException {
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        try {
            return authDAO.getAuth(authToken).username();
        } catch (DataAccessException e) {
            sendError(session,"Error, unauthorized token");
            return null;
        }
    }

    private static void broadcast(int gameID, Session noContact, ServerMessage message) throws IOException {
        Gson gson = new Gson();
        var contactList = new ArrayList<Session>();
        for (var session : sessions.keySet()) {
            if (sessions.get(session) == gameID && session != noContact) {
                contactList.add(session);
            }
        }
        for (var session : contactList) {
            session.getRemote().sendString(gson.toJson(message));
        }
    }

    private static void send(Session session, ServerMessage message) throws IOException {
        Gson gson = new Gson();
        session.getRemote().sendString(gson.toJson(message));
    }

    private static void sendError(Session session, String message) throws IOException {
        Gson gson = new Gson();
        ServerMessage errorMessage = new ErrorMessage(message);
        session.getRemote().sendString(gson.toJson(errorMessage));
    }

    private static GameData getGameData(int gameID, Session session) throws IOException {
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        try {
            return gameDAO.getGame(gameID);
        } catch (DataAccessException e) {
            sendError(session,"Error, game does not exist");
            return null;
        }
    }
}

package server;
import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MySQLAuthDAO;
import dataaccess.MySQLGameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;

import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@WebSocket
public class WebSocketHandler {
    private HashMap<Session, Integer> sessions = new HashMap<>();





    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        Gson gson = new Gson();
        UserGameCommand gameCommand = gson.fromJson(message, UserGameCommand.class);
        switch (gameCommand.getCommandType()) {
            case CONNECT -> connectUser(gameCommand.getGameID(), session, gameCommand.getAuthString(),gameCommand.getColorCode());
            case MAKE_MOVE -> makeMove(gameCommand.getGameID(),gameCommand.getMove(),session, gameCommand.getAuthString());
            case LEAVE -> leaveGame(gameCommand.getGameID(), session, gameCommand.getAuthString());
            case RESIGN -> resign(gameCommand.getGameID(), session, gameCommand.getAuthString());
        }
        try {
            session.getRemote().sendString(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resign(int gameID, Session session, String authString) {

    }

    private void leaveGame(int gameID, Session session, String authString) throws IOException {
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

        ServerMessage leftBroadcastMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        leftBroadcastMessage.setMessageBody(username + " has left the game.");
        broadcast(gameID,session,leftBroadcastMessage);
        sessions.remove(session);

        ServerMessage leftMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        leftMessage.setMessageBody("Successfully left the game");
        send(session,leftMessage);
    }

    private void makeMove(int gameID, ChessMove move, Session session, String authString) throws IOException {
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



        ServerMessage madeMoveMessageBroadcast = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        madeMoveMessageBroadcast.setMessageBody(username + " has moved " + move);
        madeMoveMessageBroadcast.setGame(game);
        broadcast(gameID,session,madeMoveMessageBroadcast);

        ServerMessage madeMoveMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        madeMoveMessage.setGame(game);
        send(session,madeMoveMessage);
    }

    private void connectUser(int gameID, Session session,String authString, int color) throws IOException {

        sessions.put(session,gameID);
        String username;
        GameData game = getGameData(gameID, session);

        username = getUsername(authString,session);
        if (username == null || game == null) {
            return;
        }


        ServerMessage joinMessageBroadcast = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        String team = switch (color) {
            case 1 -> "white";
            case 2 -> "black";
            case 3 -> "an observer";
            default -> throw new RuntimeException("Unexpected value: " + color);
        };
        joinMessageBroadcast.setMessageBody(username + " has joined the game as " + team);
        broadcast(gameID,session,joinMessageBroadcast);

        ServerMessage joinMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        joinMessage.setMessageBody("You have successfully joined the game");
        joinMessage.setGame(game.game());
        send(session, joinMessage);
    }

    String getUsername(String authToken, Session session) throws IOException {
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        try {
            return authDAO.getAuth(authToken).username();
        } catch (DataAccessException e) {
            sendError(session,"Error, unauthorized token");
            return null;
        }
    }

    void broadcast(int gameID, Session noContact, ServerMessage message) throws IOException {
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

    void send(Session session, ServerMessage message) throws IOException {
        Gson gson = new Gson();
        session.getRemote().sendString(gson.toJson(message));
    }

    void sendError(Session session, String message) throws IOException {
        Gson gson = new Gson();
        ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        errorMessage.setMessageBody(message);
        session.getRemote().sendString(gson.toJson(errorMessage));
    }

    GameData getGameData(int gameID, Session session) throws IOException {
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        try {
            return gameDAO.getGame(gameID);
        } catch (DataAccessException e) {
            sendError(session,"Error, game does not exist");
            return null;
        }
    }
}

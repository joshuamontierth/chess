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
    private HashMap<Session, Integer> sessions;





    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        Gson gson = new Gson();
        UserGameCommand gameCommand = gson.fromJson(message, UserGameCommand.class);
        switch (gameCommand.getCommandType()) {
            case CONNECT -> connectUser(gameCommand.getGameID(), session, gameCommand.getAuthString(),gameCommand.getColorCode());
            case MAKE_MOVE -> makeMove(gameCommand.getGameID(),gameCommand.getMove(),session, gameCommand.getAuthString());
            case LEAVE -> leaveGame(gameCommand.getGameID(), session);
            case RESIGN -> resign(gameCommand.getGameID(), session);
        }
        try {
            session.getRemote().sendString(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resign(int gameID, Session session) {

    }

    private void leaveGame(int gameID, Session session) {
    }

    private void makeMove(int gameID, ChessMove move, Session session, String authString) throws IOException {
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        GameData gameData;
        try {
            gameData = gameDAO.getGame(gameID);
        } catch (DataAccessException e) {
            sendError(session,"Error, game does not exist");
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
        Gson gson = new Gson();
        String username = null;
        try {
            username = getUsername(authString);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        ServerMessage madeMoveMessageBroadcast = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        madeMoveMessageBroadcast.setMessageBody(username + " has moved " + move);
        madeMoveMessageBroadcast.setGame(game);
        broadcast(gameID,session,gson.toJson(madeMoveMessageBroadcast));

        ServerMessage madeMoveMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        madeMoveMessage.setGame(game);
        send(session,gson.toJson(madeMoveMessage));


    }

    private void connectUser(int gameID, Session session,String authString, int color) throws IOException {

        sessions.put(session,gameID);
        String username;
        Gson gson = new Gson();
        ChessGame game;

        try {
            username = getUsername(authString);
        } catch (DataAccessException e) {
            sendError(session,"Error, unauthorized token");
            return;
        }
        try {
            game = getGame(gameID);
        } catch (DataAccessException e) {
            sendError(session,"Error, game does not exist");
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
        broadcast(gameID,session,gson.toJson(joinMessageBroadcast));

        ServerMessage joinMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        joinMessage.setMessageBody("You have successfully joined the game");
        joinMessage.setGame(game);
        send(session, gson.toJson(joinMessage));







    }

    String getUsername(String authToken) throws DataAccessException {
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        return authDAO.getAuth(authToken).username();
    }

    void broadcast(int gameID, Session noContact, String message) throws IOException {
        var contactList = new ArrayList<Session>();
        for (var session : sessions.keySet()) {
            if (sessions.get(session) == gameID && session != noContact) {
                contactList.add(session);
            }
        }
        for (var session : contactList) {
            session.getRemote().sendString(message);
        }
    }

    void send(Session session, String message) throws IOException {
        session.getRemote().sendString(message);
    }

    void sendError(Session session, String message) throws IOException {
        Gson gson = new Gson();
        ServerMessage errorMessage = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
        errorMessage.setMessageBody(message);
        session.getRemote().sendString(gson.toJson(errorMessage));
    }

    ChessGame getGame(int gameID) throws DataAccessException {
        return new MySQLGameDAO().getGame(gameID).game();
    }


}

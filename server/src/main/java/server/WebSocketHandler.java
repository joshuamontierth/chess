package server;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MySQLAuthDAO;
import org.eclipse.jetty.websocket.api.annotations.*;
import spark.Spark;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@WebSocket
public class WebSocketHandler {
    private HashMap<Session, Integer> sessions;





    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        Gson gson = new Gson();
        UserGameCommand gameCommand = gson.fromJson(message, UserGameCommand.class);
        switch (gameCommand.getCommandType()) {
            case CONNECT -> connectUser(gameCommand.getGameID(), session, gameCommand.getAuthString());
            case MAKE_MOVE -> makeMove(gameCommand.getGameID(),gameCommand.getMove(),session);
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

    private void makeMove(int gameID, ChessMove move, Session session) {
    }

    private void connectUser(int gameID, Session session,String authString) {
        sessions.put(session,gameID);


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


}

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
    private static HashMap<Session, Integer> sessions = new HashMap<>();
    private static HashMap<String, Session> userMap = new HashMap<>();


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
        ServerMessage resignBroadcastMessage = new NotificationMessage(username + " has resigned. " + color + " won!");
        broadcast(gameID,session,resignBroadcastMessage);

        ServerMessage resignMessage = new NotificationMessage("You have resigned.");
        send(session,resignMessage);
    }

    public static void leaveGame(int gameID, Session session, String authString) throws IOException {
        String username = getUsername(authString,session);
        GameData game = getGameData(gameID, session);
        if (username == null || game == null) {
            return;
        }
        GameData newGame = createNewGame(gameID, game, username);
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        try {
            gameDAO.updateGame(newGame);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        ServerMessage leftBroadcastMessage = new NotificationMessage(username + " has left the game.");
        broadcast(gameID,session,leftBroadcastMessage);
        sessions.remove(session);
        userMap.remove(username);

        ServerMessage leftMessage = new NotificationMessage("You have left the game.");
        send(session,leftMessage);
    }

    private static GameData createNewGame(int gameID, GameData game, String username) {
        GameData newGame;
        String whiteUsername = null;
        String blackUsername = null;
        if (game.blackUsername() != null) {
            blackUsername = game.blackUsername();
        }
        if (game.whiteUsername() != null) {
            whiteUsername = game.whiteUsername();
        }
        if (blackUsername != null && blackUsername.equals(username)) {
            newGame = new GameData(gameID, whiteUsername, null, game.gameName(), game.game());
        }
        else if(whiteUsername != null && whiteUsername.equals(username)) {
            newGame = new GameData(gameID, null, blackUsername, game.gameName(), game.game());
        }
        else {
            newGame = game;
        }
        return newGame;
    }

    public static void makeMove(int gameID, ChessMove move, Session session, String authString) throws IOException {
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        GameData gameData = getGameData(gameID,session);
        String username = getUsername(authString,session);
        if (username == null || gameData == null) {
            return;
        }
        ChessGame game = gameData.game();
        ChessGame.TeamColor userColor = username.equals(gameData.whiteUsername()) ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
        if (game.getTeamTurn() != userColor) {
            sendError(session, "Error, not your turn!");
        }

        try {
            System.out.println(move);
            game.makeMove(move);
        } catch (InvalidMoveException e) {
            if (e.getMessage() != null) {
                sendError(session,e.getMessage());
            }
            else {
                sendError(session, "Error, invalid move");
            }
            return;
        }
        GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);



        ServerMessage madeMoveMessageBroadcast = new LoadGameMessage(username + " has moved " + move,newGameData);
        broadcast(gameID,session,madeMoveMessageBroadcast);

        ServerMessage madeMoveMessage = new LoadGameMessage(null, newGameData);
        send(session,madeMoveMessage);

        specialStateCheck(gameID, game, gameData);
        try {
            gameDAO.updateGame(newGameData);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void specialStateCheck(int gameID,ChessGame game, GameData gameData) throws IOException {
        String opponentUsername = game.getTeamTurn()== ChessGame.TeamColor.WHITE ? gameData.whiteUsername() : gameData.blackUsername();
        Session opponentSession = userMap.get(opponentUsername);
        if (game.isInCheckmate(game.getTeamTurn())) {
            game.setGameComplete();
            String winningColor = game.oppositeTeam(game.getTeamTurn()).toString().toLowerCase();
            ServerMessage checkmateMessageBroadcast = new NotificationMessage(opponentUsername + " is in checkmate, " + winningColor + " won!");
            broadcast(gameID, opponentSession,checkmateMessageBroadcast);

            ServerMessage checkmateMessage = new NotificationMessage("You are in checkmate. You lose!");
            send(opponentSession,checkmateMessage);
        }
        else if(game.isInStalemate(game.getTeamTurn())) {
            game.setGameComplete();;
            ServerMessage stalemateMessageBroadcast = new NotificationMessage(opponentUsername + " is in stalemate. It's a draw!");
            broadcast(gameID, opponentSession,stalemateMessageBroadcast);

            ServerMessage stalemateMessage = new NotificationMessage("You are in stalemate. It's a draw!");
            send(opponentSession,stalemateMessage);
        }
        else if (game.isInCheck(game.getTeamTurn())) {
            ServerMessage checkMessageBroadcast = new NotificationMessage(opponentUsername + " is in check.");
            broadcast(gameID, opponentSession,checkMessageBroadcast);

            ServerMessage checkMessage = new NotificationMessage("You are in check!");
            send(opponentSession,checkMessage);

        }
    }
    public static void connectUser(int gameID, Session session,String authString) throws IOException {

        sessions.put(session,gameID);

        String username;
        GameData game = getGameData(gameID, session);

        username = getUsername(authString,session);
        if (username == null || game == null) {
            return;
        }
        userMap.put(username,session);
        String team;
        if (username.equals(game.blackUsername())) {
            team = "black";
        }
        else if (username.equals(game.whiteUsername())) {
            team = "white";
        }
        else {
            team = "an observer";
        }


        ServerMessage joinMessageBroadcast = new NotificationMessage(username + " has joined the game as " + team);
        broadcast(gameID,session,joinMessageBroadcast);
        String joinMessageString = team.equals("an observer") ? "You have successfully joined the game as an observer" : "You have successfully joined the game";
        ServerMessage joinMessage = new LoadGameMessage(joinMessageString,game);
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

package ui;

import com.google.gson.Gson;
import model.GameData;
import clientutilities.ClientConnector;
import utilities.HTMLException;
import utilities.request.*;
import utilities.result.*;

import java.util.Collection;

public class ServerFacade {
    private final String urlStub;
    private final ClientConnector clientConnector = new ClientConnector();

    public ServerFacade(String hostname, int port) {
        urlStub =  "http://" + hostname + ":" + port;
    }

    public String login(String username, String password) throws HTMLException {
        LoginRequest req = new LoginRequest(username, password);
        String url = urlStub + "/session";
        String responseString = interfaceWithConnector(url,req,null, "post");
        Gson gson = new Gson();
        LoginResult res = gson.fromJson(responseString,LoginResult.class);
        return res.authToken();
    }
    public String register(String username, String password, String email) throws HTMLException {
        RegisterUserRequest req = new RegisterUserRequest(username, password,email);
        String url = urlStub + "/user";

        Gson gson = new Gson();
        String responseString = interfaceWithConnector(url,req,null, "post");
        RegisterUserResult res = gson.fromJson(responseString,RegisterUserResult.class);
        return res.authToken();
    }
    public void logout(String authToken) throws HTMLException {
        String url = urlStub + "/session";
        interfaceWithConnector(url,null,authToken, "delete");
    }
    public int createGame(String gameName, String authToken) throws HTMLException {
        String url = urlStub + "/game";
        CreateGameRequest req = new CreateGameRequest(authToken, gameName);
        String responseString = interfaceWithConnector(url,req,authToken, "post");
        Gson gson = new Gson();
        CreateGameResult res = gson.fromJson(responseString,CreateGameResult.class);
        return res.gameID();


    }
    public Collection<GameData> listGames(String authToken) throws HTMLException {
        String url = urlStub + "/game";
        Gson gson = new Gson();
        String responseString = interfaceWithConnector(url,null,authToken, "get");
        ListGamesResult res = gson.fromJson(responseString,ListGamesResult.class);
        return res.games();

    }
    public void joinGame(int gameID, int colorSelect, String authToken) throws HTMLException {
        String url = urlStub + "/game";
        String colorString = null;
        if (colorSelect == 1) {
            colorString = "WHITE";
        }
        else if (colorSelect == 2) {
            colorString = "BLACK";
        }
        JoinGameRequest req = new JoinGameRequest(authToken,colorString,gameID);
        interfaceWithConnector(url,req,authToken, "put");

    }

    private String interfaceWithConnector(String url, Object req, String authToken, String method) throws HTMLException {
        String responseString = null;

        try {
            switch (method) {
                case "post":
                    responseString = clientConnector.post(url, req, authToken);
                    break;

                case "delete":
                    clientConnector.delete(url, authToken);
                    break;
                case "get":
                    responseString = clientConnector.get(url, authToken);
                    break;
                case "put":
                    clientConnector.put(url, req, authToken);
                    break;
                default:
                    throw new RuntimeException("Method not supported");
            }
        }
        catch (Exception e) {
            if (e instanceof HTMLException) {
                throw (HTMLException) e;
            }
            else {
                throw new RuntimeException(e.getMessage());
            }
        }
        return responseString;
    }

}

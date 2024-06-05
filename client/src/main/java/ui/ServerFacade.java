package ui;

import com.google.gson.Gson;
import service.HTMLException;
import utilities.*;

public class ServerFacade {
    private final String urlStub;
    private ClientConnector clientConnector;

    public ServerFacade(String hostname, int port) {
        urlStub =  "http://"+ hostname + ":" + port;
        clientConnector = new ClientConnector();
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
    private String interfaceWithConnector(String url, Object req, String authToken, String method) throws HTMLException {
        String responseString = null;

        try {
            switch (method) {
                case "post":
                    responseString = clientConnector.post(url, req, authToken);
                    break;

                case "delete":
                    clientConnector.delete(url, req, authToken);
                    break;
                case "get":
                    responseString = clientConnector.get(url, req, authToken);
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
    public void createGame(String gameName, String authToken) throws HTMLException {
        String url = urlStub + "/game";
        CreateGameRequest req = new CreateGameRequest(authToken, gameName);
        Gson gson = new Gson();
        String responseString = interfaceWithConnector(url,req,authToken, "post");
        CreateGameResult res = gson.fromJson(responseString,CreateGameResult.class);
        System.out.println(res.gameID());

    }
}

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
        String responseString = interfaceWithConnector(url,req,null);
        Gson gson = new Gson();
        LoginResult res = gson.fromJson(responseString,LoginResult.class);
        return res.authToken();
    }
    public String register(String username, String password, String email) throws HTMLException {
        RegisterUserRequest req = new RegisterUserRequest(username, password,email);
        String url = urlStub + "/user";

        Gson gson = new Gson();
        String responseString = interfaceWithConnector(url,req,null);
        RegisterUserResult res = gson.fromJson(responseString,RegisterUserResult.class);
        return res.authToken();
    }
    private String interfaceWithConnector(String url, Object req, String authToken) throws HTMLException {
        String responseString;
        try {
            responseString = clientConnector.post(url, req, authToken);
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

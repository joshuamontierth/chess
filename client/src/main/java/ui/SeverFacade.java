package ui;

import com.google.gson.Gson;
import utilities.*;

public class SeverFacade {
    String urlStub;
    ClientConnector clientConnector;

    public SeverFacade(String hostname, int port) {
        urlStub =  "http"+ hostname + port;
        clientConnector = new ClientConnector();

    }

    public String login(String username, String password) {
        LoginRequest req = new LoginRequest(username, password);
        String url = urlStub + "/session";
        String responseString;
        try {
            responseString = clientConnector.post(url, req, null);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        Gson gson = new Gson();
        LoginResult res = gson.fromJson(responseString,LoginResult.class);
        return res.authToken();
    }
    public String register(String username, String password, String email) {
        RegisterUserRequest req = new RegisterUserRequest(username, password,email);
        String url = urlStub + "/user";
        String responseString;
        try {
            responseString = clientConnector.post(url, req, null);
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        Gson gson = new Gson();
        RegisterUserResult res = gson.fromJson(responseString,RegisterUserResult.class);
        return res.authToken();
    }
}

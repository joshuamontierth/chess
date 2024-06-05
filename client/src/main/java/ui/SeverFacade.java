package ui;

import utilities.LoginRequest;

public class SeverFacade {
    String urlStub;
    public SeverFacade(String hostname, int port) {
        urlStub =  "http"+ hostname + port;
    }

    public String login(String username, String password) {
        LoginRequest req = new LoginRequest(username, password);


    }
    public String register(String username, String password, String email) {

    }
}

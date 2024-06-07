package client;

import org.junit.jupiter.api.*;
import server.Server;
import ui.ClientConnector;
import ui.ServerFacade;
import utilities.HTMLException;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static String authToken;

    @BeforeAll
    public static void init() throws Exception {

        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        ClientConnector cc = new ClientConnector();
        cc.delete("http://localhost:"+port+"/db",null);
        serverFacade = new ServerFacade("localhost",port);
        serverFacade.register("username","password","email");
        serverFacade.register("username2","password","email");
        authToken = serverFacade.login("username","password");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void loginPositiveTest() throws HTMLException {
        String authToken = serverFacade.login("username2","password");
        Assertions.assertFalse(authToken.isEmpty());
    }
    @Test
    public void loginNegativeTest() {
        Assertions.assertThrows(HTMLException.class, () -> serverFacade.login("username3","password"));

    }
    @Test
    public void registerPositiveTest() throws HTMLException {
        String authToken = serverFacade.register("superman","password","email");
        Assertions.assertFalse(authToken.isEmpty());

    }
    @Test
    public void registerNegativeTest() {
        Assertions.assertThrows(HTMLException.class, () -> serverFacade.register("username","password","email"));
    }
    @Test
    public void logoutPositiveTest() throws HTMLException {
        serverFacade.logout(authToken);
        Assertions.assertThrows(HTMLException.class, () -> serverFacade.createGame("game",authToken));
        authToken = serverFacade.login("username","password");

    }
    @Test
    public void logoutNegativeTest() throws HTMLException {
        //no logout twice
        serverFacade.logout(authToken);
        Assertions.assertThrows(HTMLException.class, () -> serverFacade.logout(null));
        authToken = serverFacade.login("username","password");


    }
    @Test
    public void createGamePositiveTest() throws HTMLException {
        int numGames = serverFacade.listGames(authToken).size();
        serverFacade.createGame("myGame",authToken);
        Assertions.assertEquals(serverFacade.listGames(authToken).size(), numGames + 1);
    }
    @Test
    public void createGameNegativeTest() {
        Assertions.assertThrows(HTMLException.class, () -> serverFacade.createGame("game",null));
    }
    @Test
    public void joinGamePositiveTest() throws HTMLException {
        int gameID = serverFacade.createGame("coolGame",authToken);
        serverFacade.joinGame(gameID,1,authToken);
        var games = serverFacade.listGames(authToken);
        for (var game : games) {
            if (game.gameID() == gameID) {
                Assertions.assertEquals(game.whiteUsername(),"username");
                return;
            }
        }
        Assertions.fail();
    }
    @Test
    public void joinGameNegativeTest() throws HTMLException {
        // invalid gameID
        Assertions.assertThrows(HTMLException.class, () -> serverFacade.joinGame(-1,1,authToken));

    }
    @Test
    public void listGamesPositiveTest() throws HTMLException {
        int numGames = serverFacade.listGames(authToken).size();
        serverFacade.createGame("1",authToken);
        serverFacade.createGame("2",authToken);
        serverFacade.createGame("3",authToken);
        Assertions.assertEquals(serverFacade.listGames(authToken).size(), numGames + 3);

    }
    @Test
    public void listGamesNegativeTest() {
        //need authToken
        Assertions.assertThrows(HTMLException.class, () -> serverFacade.listGames(null));

    }


}

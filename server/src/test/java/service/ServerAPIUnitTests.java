package service;
import dataaccess.*;
import org.junit.jupiter.api.*;

import utilities.*;


public class ServerAPIUnitTests {

    String authToken;
    int myGameID;
    @BeforeEach
    public void setup() throws HTMLException {
        RegisterUserResult user1 = RegisterUserService.registerUser(new RegisterUserRequest("Clark","Kent","normalperson@email.com"));
        RegisterUserResult user2 = RegisterUserService.registerUser(new RegisterUserRequest("Super","Man","superhero@email.com"));
        authToken = LoginService.login(new LoginRequest("Super","Man")).authToken();
        myGameID = CreateGameService.createGame(new CreateGameRequest(authToken,"Truth, justice, and the american way")).gameID();

    }

    @AfterEach
    public void teardown() {
        ClearService.clear(new ClearRequest());
    }


    @Test
    public void registerUserSuccessTest() throws Exception {
        //normal behavior
        RegisterUserResult expected = new RegisterUserResult("username","12345");
        RegisterUserResult actual = RegisterUserService.registerUser(new RegisterUserRequest("username","password","email@email.com"));
        Assertions.assertEquals(expected.username(),actual.username());
        Assertions.assertNotNull(actual.authToken());
    }
    @Test
    public void registerUserFailTest() {
        //No username given
        Assertions.assertThrows(HTMLException.class,() -> RegisterUserService.registerUser(new RegisterUserRequest("","password","email@email.com")));
    }
    @Test
    public void loginUserSuccessTest() throws Exception {
        //normal behavior
        LoginResult expected = new LoginResult("Clark","12345");
        LoginResult actual = LoginService.login(new LoginRequest("Clark","Kent"));
        Assertions.assertEquals(expected.username(),actual.username());
        Assertions.assertNotNull(actual.authToken());
    }
    @Test
    public void loginUserFailTest() {
        //Wrong password
        Assertions.assertThrows(HTMLException.class,() -> LoginService.login(new LoginRequest("Clark","password")));
    }
    @Test
    public void logoutUserSuccessTest() throws Exception {
        //normal behavior
        LogoutResult expected = new LogoutResult();
        LogoutResult actual = LogoutService.logout(new LogoutRequest(authToken));
        Assertions.assertEquals(expected,actual);
    }
    @Test
    public void logoutUserFailTest() {
        //No auth token
        Assertions.assertThrows(HTMLException.class,() -> LogoutService.logout(new LogoutRequest(null)));
    }
    @Test
    public void createGameSuccessTest() throws Exception {
        //normal behavior

        CreateGameResult actual = CreateGameService.createGame(new CreateGameRequest(authToken,"Justice League"));
        Assertions.assertTrue(actual.gameID()>0);
    }
    @Test
    public void createGameFailTest() {
        //No game name
        Assertions.assertThrows(HTMLException.class,() -> CreateGameService.createGame(new CreateGameRequest(authToken,null)));
    }
    @Test
    public void joinGameSuccessTest() throws Exception {
        //normal behavior
        int gameID = CreateGameService.createGame(new CreateGameRequest(authToken,"Lois Lane")).gameID();
        JoinGameService.joinGame(new JoinGameRequest(authToken,"WHITE",gameID));
        GameDAOInterface gameDAO = new MySQLGameDAO();
        String username = gameDAO.getGame(gameID).whiteUsername();
        Assertions.assertEquals(username,"Super");

    }
    @Test
    public void joinGameFailTest() {
        //Try to join as green
        Assertions.assertThrows(HTMLException.class,() -> JoinGameService.joinGame(new JoinGameRequest(authToken,"GREEN",myGameID)));
    }
    @Test
    public void listGamesSuccessTest() throws Exception {
        //normal behavior
        CreateGameService.createGame(new CreateGameRequest(authToken,"Flash"));
        CreateGameService.createGame(new CreateGameRequest(authToken,"Batman"));
        CreateGameService.createGame(new CreateGameRequest(authToken,"Aquaman"));
        CreateGameService.createGame(new CreateGameRequest(authToken,"Wonder Woman"));
        int numGames = ListGamesService.listGames(new ListGamesRequest(authToken)).games().size();
        Assertions.assertEquals(numGames,5);

    }
    @Test
    public void listGamesFailTest() {
        //No auth token
        Assertions.assertThrows(HTMLException.class,() -> ListGamesService.listGames(new ListGamesRequest(null)));
    }

    @Test
    public void clearSuccessTest() throws Exception {
        //make sure it clears all databases
        CreateGameService.createGame(new CreateGameRequest(authToken,"Robin"));
        ClearService.clear(new ClearRequest());
        Assertions.assertEquals(new MemoryAuthDAO().getSize(),0);
        Assertions.assertEquals(new MemoryGameDAO().getSize(),0);
        Assertions.assertEquals(new MemoryUserDAO().getSize(),0);
    }









}

package service;
import dataaccess.GameDAOInterface;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import org.junit.jupiter.api.*;

import utilities.*;


public class ServerAPIUnitTests {

    String authToken;
    int myGameID;
    @BeforeEach
    public void setup() throws HTMLException {
        ClearService.clear(new ClearRequest());
        RegisterUserResult user1 = RegisterUserService.registerUser(new RegisterUserRequest("Clark","Kent","normalperson@email.com"));
        RegisterUserResult user2 = RegisterUserService.registerUser(new RegisterUserRequest("Super","Man","superhero@email.com"));
        authToken = LoginService.login(new LoginRequest("Super","Man")).authToken();
        myGameID = CreateGameService.createGame(new CreateGameRequest(authToken,"Truth, justice, and the american way")).gameID();

    }

    @Test
    public void RegisterUserSuccessTest() throws Exception {
        //normal behavior
        RegisterUserResult expected = new RegisterUserResult("username","12345");
        RegisterUserResult actual = RegisterUserService.registerUser(new RegisterUserRequest("username","password","email@email.com"));
        Assertions.assertEquals(expected.username(),actual.username());
        Assertions.assertNotNull(actual.authToken());
    }
    @Test
    public void RegisterUserFailTest() {
        //No username given
        Assertions.assertThrows(HTMLException.class,() -> RegisterUserService.registerUser(new RegisterUserRequest("","password","email@email.com")));
    }
    @Test
    public void LoginUserSuccessTest() throws Exception {
        //normal behavior
        LoginResult expected = new LoginResult("Clark","12345");
        LoginResult actual = LoginService.login(new LoginRequest("Clark","Kent"));
        Assertions.assertEquals(expected.username(),actual.username());
        Assertions.assertNotNull(actual.authToken());
    }
    @Test
    public void LoginUserFailTest() {
        //Wrong password
        Assertions.assertThrows(HTMLException.class,() -> LoginService.login(new LoginRequest("Clark","password")));
    }
    @Test
    public void LogoutUserSuccessTest() throws Exception {
        //normal behavior
        LogoutResult expected = new LogoutResult();
        LogoutResult actual = LogoutService.logout(new LogoutRequest(authToken));
        Assertions.assertEquals(expected,actual);
    }
    @Test
    public void LogoutUserFailTest() {
        //No auth token
        Assertions.assertThrows(HTMLException.class,() -> LogoutService.logout(new LogoutRequest(null)));
    }
    @Test
    public void CreateGameSuccessTest() throws Exception {
        //normal behavior
        CreateGameResult expected = new CreateGameResult(2);
        CreateGameResult actual = CreateGameService.createGame(new CreateGameRequest(authToken,"Justice League"));
        Assertions.assertEquals(expected,actual);
    }
    @Test
    public void CreateGameFailTest() {
        //No game name
        Assertions.assertThrows(HTMLException.class,() -> CreateGameService.createGame(new CreateGameRequest(authToken,null)));
    }
    @Test
    public void JoinGameSuccessTest() throws Exception {
        //normal behavior
        JoinGameResult actual = JoinGameService.joinGame(new JoinGameRequest(authToken,"WHITE",myGameID));
        GameDAOInterface gameDAO = new MemoryGameDAO();
        String username = gameDAO.getGame(myGameID).whiteUsername();
        Assertions.assertEquals(username,"Super");

    }
    @Test
    public void JoinGameFailTest() {
        //Try to join as green
        Assertions.assertThrows(HTMLException.class,() -> JoinGameService.joinGame(new JoinGameRequest(authToken,"GREEN",myGameID)));
    }
    @Test
    public void ListGamesSuccessTest() throws Exception {
        //normal behavior
        CreateGameService.createGame(new CreateGameRequest(authToken,"Flash"));
        CreateGameService.createGame(new CreateGameRequest(authToken,"Batman"));
        CreateGameService.createGame(new CreateGameRequest(authToken,"Aquaman"));
        CreateGameService.createGame(new CreateGameRequest(authToken,"Wonder Woman"));
        int numGames = ListGamesService.listGames(new ListGamesRequest(authToken)).games().size();
        Assertions.assertEquals(numGames,5);

    }
    @Test
    public void ListGamesFailTest() {
        //No auth token
        Assertions.assertThrows(HTMLException.class,() -> ListGamesService.listGames(new ListGamesRequest(null)));
    }

    @Test
    public void ClearSuccessTest() throws Exception {
        //make sure it clears all databases
        CreateGameService.createGame(new CreateGameRequest(authToken,"Robin"));
        ClearService.clear(new ClearRequest());
        Assertions.assertEquals(new MemoryAuthDAO().getSize(),0);
        Assertions.assertEquals(new MemoryGameDAO().getSize(),0);
        Assertions.assertEquals(new MemoryUserDAO().getSize(),0);
    }









}

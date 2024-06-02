package dataaccess;


import chess.ChessGame;
import model.*;
import org.junit.jupiter.api.*;


import java.sql.Connection;

public class SQLTests {
    String exampleAuthToken = "8327177b-f022-4dfc-aac4-23b37427f611";
    String username = "username";
    String password = "password";
    String email = "email";
    String username2 = "username2";
    String gameName = "gameName";

    @BeforeEach
    public void setup() throws Exception  {
        String[] statements = {
                "DElETE FROM users;",
                "DElETE FROM auth;",
                "DElETE FROM games;"

                };
        try (Connection c = DatabaseManager.getConnection()) {
            for (String statement : statements) {
                try (var preparedStatement = c.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
    }

    @AfterEach
    public void teardown() {

    }



    @Test
    public void createAuthPositiveTest() throws Exception {
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        AuthData injectedAuth = new AuthData(exampleAuthToken,username);
        authDAO.createAuth(injectedAuth);

        try (Connection c = DatabaseManager.getConnection()) {
            try (var preparedStatement = c.prepareStatement("SELECT * FROM auth WHERE authToken = ?;")) {
                preparedStatement.setString(1,injectedAuth.authToken());
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    Assertions.assertEquals(injectedAuth,new AuthData(rs.getString("authToken"),rs.getString("username")));
                }
                else {
                    Assertions.fail();
                }


            }
        }

    }
    @Test
    public void createAuthNegativeTest() {
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        AuthData injectedAuth = new AuthData(null,null);
        Assertions.assertThrows(Exception.class, ()->authDAO.createAuth(injectedAuth));

    }
    @Test
    public void getAuthPositiveTest() throws Exception{
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        AuthData injectedAuth = new AuthData(exampleAuthToken,username);
        authDAO.createAuth(injectedAuth);
        AuthData retrieved = authDAO.getAuth(exampleAuthToken);
        Assertions.assertEquals(injectedAuth,retrieved);



    }
    @Test
    public void getAuthNegativeTest() {
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        Assertions.assertThrows(Exception.class, ()->authDAO.getAuth(null));

    }

    @Test
    public void deleteAuthPositiveTest() throws Exception{
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        AuthData injectedAuth = new AuthData(exampleAuthToken,username);
        authDAO.createAuth(injectedAuth);
        authDAO.deleteAuth(exampleAuthToken);
        Assertions.assertThrows(Exception.class, ()->authDAO.getAuth(exampleAuthToken));



    }
    @Test
    public void deleteAuthNegativeTest() {
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        Assertions.assertThrows(Exception.class, ()->authDAO.deleteAuth(exampleAuthToken));

    }
    @Test
    public void clearAuthPositiveTest() {
        MySQLAuthDAO authDAO = new MySQLAuthDAO();
        authDAO.createAuth(new AuthData("1","Bruce"));
        authDAO.createAuth(new AuthData("2","Steve"));
        authDAO.createAuth(new AuthData("3","Tony"));
        authDAO.createAuth(new AuthData("4","Peter"));
        authDAO.clear();
        Assertions.assertThrows(Exception.class, ()->authDAO.getAuth("1"));
        Assertions.assertThrows(Exception.class, ()->authDAO.getAuth("2"));
        Assertions.assertThrows(Exception.class, ()->authDAO.getAuth("3"));
        Assertions.assertThrows(Exception.class, ()->authDAO.getAuth("4"));
    }

    @Test
    public void createUserPositiveTest() throws Exception {
        MySQLUserDAO userDAO = new MySQLUserDAO();
        UserData injectedUser = new UserData(username,password,email);
        userDAO.createUser(injectedUser);

        try (Connection c = DatabaseManager.getConnection()) {
            try (var preparedStatement = c.prepareStatement("SELECT * FROM users WHERE username = ?;")) {
                preparedStatement.setString(1,injectedUser.username());
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    Assertions.assertEquals(injectedUser,new UserData(rs.getString("username"),rs.getString("password"),rs.getString("email")));
                }
                else {
                    Assertions.fail();
                }


            }
        }

    }
    @Test
    public void createUserNegativeTest() {
        MySQLUserDAO userDAO = new MySQLUserDAO();
        UserData injectedUser = new UserData(null,null,null);
        Assertions.assertThrows(Exception.class, ()->userDAO.createUser(injectedUser));

    }
    @Test
    public void getUserPositiveTest() throws Exception{
        MySQLUserDAO userDAO = new MySQLUserDAO();
        UserData injectedUser = new UserData(username,password,email);
        userDAO.createUser(injectedUser);
        UserData retrieved = userDAO.getUser(username);
        Assertions.assertEquals(injectedUser,retrieved);



    }
    @Test
    public void getUserNegativeTest() {
        MySQLUserDAO userDAO = new MySQLUserDAO();
        Assertions.assertThrows(Exception.class, ()->userDAO.getUser(null));

    }

    @Test
    public void clearUserPositiveTest() {
        MySQLUserDAO userDAO = new MySQLUserDAO();
        userDAO.createUser(new UserData("Bruce","P","M"));
        userDAO.createUser(new UserData("Steve","a","a"));
        userDAO.createUser(new UserData("Tony","s","i"));
        userDAO.createUser(new UserData("Peter","s","l"));
        userDAO.clear();
        Assertions.assertThrows(Exception.class, ()->userDAO.getUser("Bruce"));
        Assertions.assertThrows(Exception.class, ()->userDAO.getUser("Steve"));
        Assertions.assertThrows(Exception.class, ()->userDAO.getUser("Tony"));
        Assertions.assertThrows(Exception.class, ()->userDAO.getUser("Peter"));
    }
    @Test
    public void createGamePositiveTest() throws Exception {
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        GameData injectedGame = new GameData(0,username,null,gameName,new ChessGame());
        int id = gameDAO.createGame(injectedGame);

        try (Connection c = DatabaseManager.getConnection()) {
            try (var preparedStatement = c.prepareStatement("SELECT * FROM games WHERE gameID = ?;")) {
                preparedStatement.setInt(1,id);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    Assertions.assertEquals(username,rs.getString("whiteUsername"));
                }
                else {
                    Assertions.fail();
                }


            }
        }

    }
    @Test
    public void createGameNegativeTest() {
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        GameData injectedGame = new GameData(0,null,null,null,null);
        Assertions.assertThrows(Exception.class, ()->gameDAO.createGame(injectedGame));

    }
    @Test
    public void getGamePositiveTest() throws Exception{
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        GameData injectedGame = new GameData(0,username,username2,gameName,new ChessGame());
        int id = gameDAO.createGame(injectedGame);
        GameData retrieved = gameDAO.getGame(id);

        Assertions.assertEquals(injectedGame.gameName(),retrieved.gameName());



    }
    @Test
    public void getGameNegativeTest() {
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        Assertions.assertThrows(Exception.class, ()->gameDAO.getGame(0));

    }
    @Test
    public void listGamesPositiveTest() throws Exception{
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        gameDAO.createGame(new GameData(0,username, username2, "G",new ChessGame()));
        gameDAO.createGame(new GameData(0,username, username2, "A",new ChessGame()));
        gameDAO.createGame(new GameData(0,username, username2, "M",new ChessGame()));
        gameDAO.createGame(new GameData(0,username, username2, "E",new ChessGame()));
        var games = gameDAO.listGames();

        Assertions.assertEquals(games.size(),4);



    }
    @Test
    public void listGamesNegativeTest() {
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        var games = gameDAO.listGames();
        Assertions.assertEquals(games.size(),0);


    }
    @Test
    public void updateGamePositiveTest() throws Exception{
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        GameData originalGame = new GameData(1,username, username2, "This is an awesome game",new ChessGame());
        int id = gameDAO.createGame(originalGame);
        GameData updatedGame = new GameData(id,username, "Superman", "This is an awesome game",new ChessGame());
        gameDAO.updateGame(updatedGame);
        GameData retrieved = gameDAO.getGame(id);
        Assertions.assertEquals("Superman",retrieved.blackUsername());



    }
    @Test
    public void updateGameNegativeTest() {
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        Assertions.assertThrows(Exception.class, ()->gameDAO.updateGame(null));

    }

    @Test
    public void clearGamePositiveTest() {
        MySQLGameDAO gameDAO = new MySQLGameDAO();
        int id1 = gameDAO.createGame(new GameData(1,null, null, "G",new ChessGame()));
        int id2 = gameDAO.createGame(new GameData(2,null, null, "A",new ChessGame()));
        int id3 = gameDAO.createGame(new GameData(3,null, null, "M",new ChessGame()));
        int id4 = gameDAO.createGame(new GameData(4,null, null, "E",new ChessGame()));

        gameDAO.clear();
        Assertions.assertThrows(Exception.class, ()->gameDAO.getGame(id1));
        Assertions.assertThrows(Exception.class, ()->gameDAO.getGame(id2));
        Assertions.assertThrows(Exception.class, ()->gameDAO.getGame(id3));
        Assertions.assertThrows(Exception.class, ()->gameDAO.getGame(id4));
    }




}




package dataaccess;


import model.*;
import org.junit.jupiter.api.*;


import java.sql.Connection;

public class SQLTests {
    String exampleAuthToken = "8327177b-f022-4dfc-aac4-23b37427f611";
    String username = "username";
    String password = "password";
    String email = "email";

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


}

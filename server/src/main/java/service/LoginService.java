package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import utilities.*;
import utilities.request.LoginRequest;
import utilities.result.LoginResult;

import java.util.UUID;

public class LoginService {
    public static LoginResult login(LoginRequest req) throws HTMLException {
        UserDAOInterface userDAO = new MySQLUserDAO();
        UserData user;
        try {
            user = userDAO.getUser(req.username());
        } catch (DataAccessException e) {
            throw new HTMLException("Error: unauthorized",401);
        }
        if (!BCrypt.checkpw(req.password(),user.password())) {
            throw new HTMLException("Error: unauthorized",401);
        }
        String authToken = UUID.randomUUID().toString();
        AuthDAOInterface authDAO = new MySQLAuthDAO();
        authDAO.createAuth(new AuthData(authToken,req.username()));
        return new LoginResult(user.username(), authToken);
    }
}

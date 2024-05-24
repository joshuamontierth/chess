package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import utilities.*;

import java.util.UUID;

public class LoginService {
    public static LoginResult login(LoginRequest req) throws HTMLException {
        UserDAOInterface userDAO = new MemoryUserDAO();
        UserData user;
        try {
            user = userDAO.getUser(req.username());
        } catch (DataAccessException e) {
            throw new HTMLException("Error: unauthorized",401);
        }
        if (!user.password().equals(req.password())) {
            throw new HTMLException("Error: unauthorized",401);
        }
        String authToken = UUID.randomUUID().toString();
        AuthDAOInterface authDAO = new MemoryAuthDAO();
        authDAO.createAuth(new AuthData(authToken,req.username()));
        return new LoginResult(user.username(), authToken);
    }
}

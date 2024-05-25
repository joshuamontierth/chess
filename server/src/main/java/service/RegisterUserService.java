package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import utilities.*;

import java.util.UUID;

public class RegisterUserService {
    public static RegisterUserResult registerUser(RegisterUserRequest req) throws HTMLException {
        UserDAOInterface userDAO = new MemoryUserDAO();
        if (req.username() == null || req.password() == null || req.email() == null) {
            throw new HTMLException("Error: Fill out all required fields", 400);
        }
        checkIfUserExists(req,userDAO);

        userDAO.createUser(new UserData(req.username(), req.password(), req.email()));
        String authToken = UUID.randomUUID().toString();
        new MemoryAuthDAO().createAuth(new AuthData(authToken,req.username()));
        return new RegisterUserResult(req.username(), authToken);
    }
    private static void checkIfUserExists(RegisterUserRequest req, UserDAOInterface userDAO) throws HTMLException {
        try {
            userDAO.getUser(req.username());
            throw new HTMLException("Error: already taken", 403);

        } catch (DataAccessException ignored) {

        }
    }
}

package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import utilities.*;

import java.util.UUID;

public class RegisterUserService {
    public static RegisterUserResult registerUser(RegisterUserRequest req) throws HTMLException {
        UserDAOInterface userDAO = new MySQLUserDAO();
        if (req.username() == null || req.password() == null || req.email() == null) {
            throw new HTMLException("Error: Bad request", 400);
        }
        if (req.username().isEmpty() || req.password().isEmpty() || req.email().isEmpty()) {
            throw new HTMLException("Error: Fill out all required fields", 400);
        }

        checkIfUserExists(req,userDAO);

        userDAO.createUser(new UserData(req.username(), BCrypt.hashpw(req.password(), BCrypt.gensalt()), req.email()));
        String authToken = UUID.randomUUID().toString();
        new MySQLAuthDAO().createAuth(new AuthData(authToken,req.username()));
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

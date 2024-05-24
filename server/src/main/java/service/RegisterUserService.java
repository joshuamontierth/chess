package service;

import dataaccess.*;
import model.UserData;
import utilities.*;

import java.util.UUID;

public class RegisterUserService {
    public RegisterUserResult login(RegisterUserRequest req) throws HTMLException {
        UserDAOInterface userDAO = new MemoryUserDAO();
        checkIfUserExists(req,userDAO);
        userDAO.createUser(new UserData(req.username(), req.password(), req.email()));
        String authToken = UUID.randomUUID().toString();
        return new RegisterUserResult(req.username(), authToken);
    }
    private void checkIfUserExists(RegisterUserRequest req, UserDAOInterface userDAO) throws HTMLException {
        try {
            userDAO.getUser(req.username());
            throw new HTMLException("Error: already taken", 403);

        } catch (DataAccessException ignored) {

        }
    }
}

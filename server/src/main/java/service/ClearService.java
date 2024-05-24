package service;

import dataaccess.*;
import utilities.ClearRequest;
import utilities.ClearResult;

public class ClearService {
    public static ClearResult clear(ClearRequest req) {
        AuthDAOInterface authDAO = new MemoryAuthDAO();
        authDAO.clear();
        GameDAOInterface gameDAO = new MemoryGameDAO();
        gameDAO.clear();
        UserDAOInterface userDAO = new MemoryUserDAO();
        userDAO.clear();
        return new ClearResult();
    }
}

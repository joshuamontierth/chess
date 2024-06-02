package service;

import dataaccess.*;
import utilities.ClearRequest;
import utilities.ClearResult;

public class ClearService {
    public static ClearResult clear(ClearRequest req) {
        AuthDAOInterface authDAO = new MySQLAuthDAO();
        authDAO.clear();
        GameDAOInterface gameDAO = new MySQLGameDAO();
        gameDAO.clear();
        UserDAOInterface userDAO = new MySQLUserDAO();
        userDAO.clear();
        return new ClearResult();
    }
}

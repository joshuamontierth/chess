package service;

import dataaccess.AuthDAOInterface;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;

public class Service {
    void verifyUser(String authToken) throws DataAccessException {
        AuthDAOInterface authDAO = new MemoryAuthDAO();
        authDAO.getAuth(authToken);
    }
}

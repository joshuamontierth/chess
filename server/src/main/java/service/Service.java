package service;

import dataaccess.AuthDAOInterface;
import dataaccess.MemoryAuthDAO;
import model.AuthData;

public class Service {
    static protected String verifyUser(String authToken) throws HTMLException {
        try {
            AuthDAOInterface authDAO = new MemoryAuthDAO();
            AuthData auth = authDAO.getAuth(authToken);
            return auth.username();
        }
        catch (Exception e) {
            throw new HTMLException(e.getMessage(),401);
        }
    }
}

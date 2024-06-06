package service;

import dataaccess.AuthDAOInterface;
import dataaccess.MySQLAuthDAO;
import model.AuthData;
import utilities.HTMLException;

public class Service {
    static protected String verifyUser(String authToken) throws HTMLException {
        try {
            AuthDAOInterface authDAO = new MySQLAuthDAO();
            AuthData auth = authDAO.getAuth(authToken);
            return auth.username();
        }
        catch (Exception e) {
            throw new HTMLException(e.getMessage(),401);
        }
    }
}

package service;

import dataaccess.*;
import utilities.*;

public class LogoutService extends Service {
    static public LogoutResult logout(LogoutRequest req) throws HTMLException {
        AuthDAOInterface authDAO = new MySQLAuthDAO();

        verifyUser(req.authToken());
        try {
            authDAO.deleteAuth(req.authToken());
        } catch (DataAccessException e) {
           throw new HTMLException(e.getMessage(),401);
        }

        return new LogoutResult();
    }
}

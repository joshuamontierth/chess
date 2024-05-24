package service;

import dataaccess.*;
import utilities.*;

public class LogoutService extends Service {
    public LogoutResult login(LogoutRequest req) throws HTMLException {
        AuthDAOInterface authDAO = new MemoryAuthDAO();

        verifyUser(req.authToken());
        try {
            authDAO.deleteAuth(req.authToken());
        } catch (DataAccessException e) {
           throw new HTMLException(e.getMessage(),401);
        }

        return new LogoutResult();
    }
}

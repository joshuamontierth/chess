package dataaccess;

import model.AuthData;

public interface AuthDAOInterface {
    void createAuth(AuthData auth);
    AuthData getAuth(String AuthToken) throws DataAccessException;
    void deleteAuth(String AuthToken) throws DataAccessException;
    void clear();
}

package dataaccess;

import model.AuthData;

import java.util.TreeMap;

public class MemoryAuthDAO implements AuthDAOInterface{
    static TreeMap<String,AuthData> auths = new TreeMap<>();
    @Override
    public void createAuth(AuthData auth) {
        auths.put(auth.authToken(),auth);
    }

    @Override
    public AuthData getAuth(String AuthToken) throws DataAccessException {
        AuthData auth = auths.get(AuthToken);
        if (auth == null) {
            throw new DataAccessException("Error: unauthorized");

        }
        return auth;
    }

    @Override
    public void deleteAuth(String AuthToken) throws DataAccessException {
        if (auths.containsKey(AuthToken)) {
            auths.remove(AuthToken);
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    @Override
    public void clear() {

        auths.clear();

    }
}

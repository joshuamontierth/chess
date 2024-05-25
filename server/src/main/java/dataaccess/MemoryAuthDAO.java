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
    public AuthData getAuth(String authToken) throws DataAccessException {
        AuthData auth = auths.get(authToken);
        if (auth == null) {
            throw new DataAccessException("Error: unauthorized");

        }
        return auth;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (auths.containsKey(authToken)) {
            auths.remove(authToken);
        }
        else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    @Override
    public void clear() {

        auths.clear();

    }
    public int getSize() {
        return auths.size();
    }
}

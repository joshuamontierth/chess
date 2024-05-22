package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAOInterface{
    HashMap<String,AuthData> auths;
    @Override
    public void creatAuth(AuthData auth) {
        auths.put(auth.authToken(),auth);
    }

    @Override
    public AuthData getAuth(String AuthToken) throws DataAccessException {
        AuthData auth = auths.get(AuthToken);
        if (auth == null) {
            throw new DataAccessException("AuthToken does not exist");

        }
        return auth;
    }

    @Override
    public void deleteAuth(String AuthToken) throws DataAccessException {
        if (auths.containsKey(AuthToken)) {
            auths.remove(AuthToken);
        }
        else {
            throw new DataAccessException("AuthToken does not exist");
        }
    }

    @Override
    public void clear() {
        auths.clear();
    }
}

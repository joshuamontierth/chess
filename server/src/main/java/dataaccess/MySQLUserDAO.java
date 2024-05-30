package dataaccess;

import model.UserData;

public class MySQLUserDAO implements UserDAOInterface {
    @Override
    public void createUser(UserData user) {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() {

    }
}

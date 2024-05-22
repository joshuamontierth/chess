package dataaccess;

import model.UserData;

public interface UserDAOInterface {
    void createUser(UserData user);
    UserData getUser(String username) throws DataAccessException;
    void clear();

}

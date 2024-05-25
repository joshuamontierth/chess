package dataaccess;

import model.UserData;


import java.util.TreeMap;

public class MemoryUserDAO implements UserDAOInterface {
    static TreeMap<String, UserData> users = new TreeMap<>();


    @Override
    public void createUser(UserData user) {
        users.put(user.username(),user);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        UserData user = users.get(username);
        if (user == null) {
            throw new DataAccessException("User does not exist");
        }
        return user;

    }

    @Override
    public void clear() {
        users.clear();
    }
    public int getSize() {
        return users.size();
    }

}

package dataaccess;


import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLUserDAO implements UserDAOInterface {
    public MySQLUserDAO() {
        try {
            DatabaseManager.createDatabase();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createUser(UserData user) {
        try (Connection c = DatabaseManager.getConnection()) {
            try (var preparedStatement = c.prepareStatement("INSERT INTO users (username,password,email ) VALUES (?, ?, ?);")) {
                preparedStatement.setString(1, user.username());
                preparedStatement.setString(2, user.password());
                preparedStatement.setString(3, user.email());
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection c = DatabaseManager.getConnection()) {
            try (var preparedStatement = c.prepareStatement("SELECT * FROM users WHERE username = ?;")) {
                preparedStatement.setString(1,username);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return new UserData(rs.getString("username"),rs.getString("password"),rs.getString("email"));
                }
                else {
                    throw new DataAccessException("User does not exist");
                }
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        try (Connection c = DatabaseManager.getConnection()) {
            try (var preparedStatement = c.prepareStatement("DELETE FROM users;")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}

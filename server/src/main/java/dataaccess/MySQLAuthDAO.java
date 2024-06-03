package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLAuthDAO implements AuthDAOInterface{
    public MySQLAuthDAO() {
        try {
            DatabaseManager.createDatabase();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createAuth(AuthData auth)  {
        try (Connection c = DatabaseManager.getConnection()) {
            try (var preparedStatement = c.prepareStatement("INSERT INTO auth (authToken, username) VALUES (?, ?);")) {
                preparedStatement.setString(1,auth.authToken());
                preparedStatement.setString(2,auth.username());
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection c = DatabaseManager.getConnection()) {
            try (var preparedStatement = c.prepareStatement("SELECT * FROM auth WHERE authToken = ?;")) {
                preparedStatement.setString(1,authToken);
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    return new AuthData(rs.getString("authToken"),rs.getString("username"));
                }
                else {
                    throw new DataAccessException("Error: unauthorized");
                }
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (Connection c = DatabaseManager.getConnection()) {
            try (var preparedStatement = c.prepareStatement("DELETE FROM auth WHERE authToken = ?;")) {
                preparedStatement.setString(1,authToken);
                if(preparedStatement.executeUpdate() != 1) {
                    throw new DataAccessException("Error: unauthorized");
                };
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void clear() {
        try (Connection c = DatabaseManager.getConnection()) {
            try (var preparedStatement = c.prepareStatement("DELETE FROM auth")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}

package dataaccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLAuthDAO implements AuthDAOInterface{
    @Override
    public void createAuth(AuthData auth)  {
        try (Connection c = DatabaseManager.getConnection()) {
            try (var preparedStatement = c.prepareStatement("INSERT INTO auth (authToken, username) VALUES (?, ?)")) {
                ResultSet rs = preparedStatement.executeQuery();
                rs.next();
                System.out.println(rs.getInt(1));
            }
        }
        catch (Exception e){

        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() {

    }
}

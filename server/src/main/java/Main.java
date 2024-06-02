
import dataaccess.DatabaseManager;
import server.Server;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseManager.createDatabase();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Server server = new Server();
        server.run(5000);
    }
}
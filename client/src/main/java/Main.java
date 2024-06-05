
import server.*;
import ui.Client;

public class Main {
    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Client: ");
        Server server = new Server();
        server.run(5000);
        Client client = new Client();
        client.run("localhost",5000);



    }
}
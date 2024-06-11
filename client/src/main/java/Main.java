

import ui.Client;
import utilities.WebsocketConnector;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("♕ 240 Chess Client: ");
//        Client client = new Client();
//        client.run("localhost",5000);

        WebsocketConnector WSConnector = new WebsocketConnector(5000);
        WSConnector.send("Hello World");



    }
}
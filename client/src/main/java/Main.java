

import com.google.gson.Gson;
import ui.Client;
import utilities.WebsocketConnector;
import websocket.commands.UserGameCommand;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("♕ 240 Chess Client: ");
//        Client client = new Client();
//        client.run("localhost",5000);

        WebsocketConnector WSConnector = new WebsocketConnector(5000);
        Gson gson = new Gson();
        UserGameCommand userGameCommand = new UserGameCommand("e7549cf4-3a4e-4bf0-9627-6feff8af0c56");
        userGameCommand.setCommandType(UserGameCommand.CommandType.CONNECT);
        userGameCommand.setGameID(34);
        userGameCommand.setColorCode(1);
        WSConnector.send(gson.toJson(userGameCommand));



    }
}
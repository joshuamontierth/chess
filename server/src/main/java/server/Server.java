package server;

import com.google.gson.Gson;

import dataaccess.DatabaseManager;
import service.*;
import spark.*;
import utilities.*;
import utilities.request.*;

public class Server {
    private final WebSocketHandler webSocketHandler;
    public Server() {
        webSocketHandler = new WebSocketHandler();
    }

    public int run(int desiredPort) {
        try {
            DatabaseManager.createDatabase();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.webSocket("/ws", webSocketHandler);

        // Register your endpoints and handle exceptions here.
        Gson gson = new Gson();
        Spark.delete("/db", (req, res) -> serialize(ClearService.clear(new ClearRequest())));
        Spark.post("/user", (req, res) -> {
            try {
                return serialize(RegisterUserService.registerUser(gson.fromJson(req.body(), RegisterUserRequest.class)));
            }
            catch (HTMLException e) {
                res.status(e.getErrorCode());
                return serialize(new ErrorResponse(e.getMessage()));
            }
        });
        Spark.post("/session", (req, res) -> {
            try {

                return serialize(LoginService.login(gson.fromJson(req.body(), LoginRequest.class)));
            }
            catch (HTMLException e) {
                res.status(e.getErrorCode());
                return serialize(new ErrorResponse(e.getMessage()));
            }
        });

        Spark.delete("/session", (req, res) -> {
            try {

                return serialize(LogoutService.logout(new LogoutRequest(req.headers("Authorization"))));
            }
            catch (HTMLException e) {
                res.status(e.getErrorCode());
                return serialize(new ErrorResponse(e.getMessage()));
            }
        });

        Spark.post("/game", (req, res) -> {
            try {
                CreateGameRequest gameName = gson.fromJson(req.body(),CreateGameRequest.class);

                return serialize(CreateGameService.createGame(new CreateGameRequest(req.headers("Authorization"),gameName.gameName())));
            }
            catch (HTMLException e) {
                res.status(e.getErrorCode());
                return serialize(new ErrorResponse(e.getMessage()));
            }
        });

        Spark.get("/game", (req, res) -> {
            try {
                return serialize(ListGamesService.listGames(new ListGamesRequest(req.headers("Authorization"))));
            }
            catch (HTMLException e) {
                res.status(e.getErrorCode());
                return serialize(new ErrorResponse(e.getMessage()));
            }
        });

        Spark.put("/game", (req, res) -> {
            try {
                JoinGameRequest request = gson.fromJson(req.body(),JoinGameRequest.class);

                return serialize(JoinGameService.joinGame(new JoinGameRequest(req.headers("Authorization"),request.playerColor(),request.gameID())));
            }
            catch (HTMLException e) {
                res.status(e.getErrorCode());
                return serialize(new ErrorResponse(e.getMessage()));
            }
        });

        Spark.awaitInitialization();
        return Spark.port();

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
    private String serialize(Object o) {
        return new Gson().toJson(o);
    }


}

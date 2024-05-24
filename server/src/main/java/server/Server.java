package server;

import com.google.gson.Gson;

import service.ClearService;
import service.RegisterUserService;
import spark.*;
import utilities.ClearRequest;
import utilities.RegisterUserRequest;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Gson gson = new Gson();
        Spark.delete("/db", (req, res) -> serialize(ClearService.clear(new ClearRequest())));
        Spark.post("/user", (req, res) -> serialize(RegisterUserService.registerUser(gson.fromJson(req.body(), RegisterUserRequest.class))));
        Spark.post("/user", (req, res) -> serialize(RegisterUserService.registerUser(gson.fromJson(req.body(), RegisterUserRequest.class))));
        Spark.post("/user", (req, res) -> serialize(RegisterUserService.registerUser(gson.fromJson(req.body(), RegisterUserRequest.class))));
        Spark.post("/user", (req, res) -> serialize(RegisterUserService.registerUser(gson.fromJson(req.body(), RegisterUserRequest.class))));
        Spark.post("/user", (req, res) -> serialize(RegisterUserService.registerUser(gson.fromJson(req.body(), RegisterUserRequest.class))));
        Spark.post("/user", (req, res) -> serialize(RegisterUserService.registerUser(gson.fromJson(req.body(), RegisterUserRequest.class))));

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

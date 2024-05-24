package server;

import com.google.gson.Gson;

import service.ClearService;
import service.LoginService;
import service.LogoutService;
import service.RegisterUserService;
import spark.*;
import utilities.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Gson gson = new Gson();
        Spark.delete("/db", (req, res) -> serialize(ClearService.clear(new ClearRequest())));
        Spark.post("/user", (req, res) -> serialize(RegisterUserService.registerUser(gson.fromJson(req.body(), RegisterUserRequest.class))));
        Spark.post("/session", (req, res) -> {
            try {
                return serialize(LoginService.login(gson.fromJson(req.body(), LoginRequest.class)));
            }
            catch (HTMLException e) {
                res.status(e.getErrorCode());
                return serialize(e.getMessage());
            }
        });

        Spark.delete("/session", (req, res) -> {
            try {

                return serialize(LogoutService.logout(new LogoutRequest(req.headers("Authorization"))));
            }
            catch (HTMLException e) {
                res.status(e.getErrorCode());
                return serialize(e.getMessage());
            }
        });

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

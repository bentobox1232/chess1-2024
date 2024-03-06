package server;

import com.google.gson.Gson;
import dataAccess.*;
import handler.*;
import spark.*;

public class Server {


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        UserDataAccess userDataAccess = new MemoryUserDataAccess();
        AuthDataAccess authDataAccess = new MemoryAuthDataAccess();
        GameDataAccess gameDataAccess = new MemoryGameDataAccess();

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", new ClearHandler(authDataAccess, gameDataAccess, userDataAccess));
        Spark.post("/user", new RegisterHandler(userDataAccess, authDataAccess));
        Spark.post("/session", new LoginHandler(userDataAccess, authDataAccess));
        Spark.delete("/session", new LogoutHandler(authDataAccess));
        Spark.get("/game", new ListHandler(gameDataAccess, authDataAccess));
        Spark.post("/game", new CreateHandler(gameDataAccess, authDataAccess));
        Spark.put("/game", new JoinHandler(gameDataAccess, authDataAccess));

        Spark.awaitInitialization();


        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
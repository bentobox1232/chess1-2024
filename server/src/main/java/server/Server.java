package server;

import dao.AuthDAO;
import dao.GameDAO;
import dao.UserDAO;
import dataAccess.*;
import handler.*;
import spark.Spark;

import java.sql.Connection;

public class Server {
//    private final WSServer wsServer;
    public int run(int desiredPort) {
        try {
//            wsServer = new WSServer();
            DatabaseManager databaseManager = DatabaseManager.getInstance();

            databaseManager.createDatabase();

            Connection databaseConnection = databaseManager.getConnection();


            UserDataAccess userDataAccess = new UserDAO(databaseConnection);
            AuthDataAccess authDataAccess = new AuthDAO(databaseConnection);
            GameDataAccess gameDataAccess = new GameDAO(databaseConnection);

            Spark.port(desiredPort);

            Spark.webSocket("/connect", WSServer.class);
//            Spark.get("/echo/:msg", (req, res) -> "HTTP response: " + req.params(":msg"));

            Spark.staticFiles.location("web");

            // Register your endpoints and handle exceptions here.
            Spark.delete("/db", new ClearHandler(authDataAccess, gameDataAccess, userDataAccess));
            Spark.post("/user", new RegisterHandler(userDataAccess, authDataAccess));
            Spark.post("/session", new LoginHandler(userDataAccess, authDataAccess));
            Spark.delete("/session", new LogoutHandler(authDataAccess));
            Spark.get("/game", new ListHandler(gameDataAccess, authDataAccess));
            Spark.post("/game", new CreateHandler(gameDataAccess, authDataAccess));
            Spark.put("/game", new JoinHandler(gameDataAccess, authDataAccess));

            // WebSocket endpoint

            Spark.awaitInitialization();

            return Spark.port();
        } catch (DataAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
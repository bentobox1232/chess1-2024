package server;

import dao.AuthDAO;
import dao.GameDAO;
import dao.UserDAO;
import dataAccess.*;
import handler.*;
import spark.*;

import java.sql.Connection;
import java.sql.SQLException;

public class Server {




    public int run(int desiredPort) {
        try {
            DatabaseManager databaseManager = DatabaseManager.getInstance();
            Connection databaseConnection = databaseManager.getConnection();

            // Create the database if it doesn't exist
            databaseManager.createDatabase();

            UserDataAccess userDataAccess = new UserDAO(databaseConnection);
            AuthDataAccess authDataAccess = new AuthDAO(databaseConnection);
            GameDataAccess gameDataAccess = new GameDAO(databaseConnection);

            Spark.port(desiredPort);
            Spark.staticFiles.location("web");

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
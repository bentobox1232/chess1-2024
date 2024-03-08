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

    private UserDataAccess userDataAccess;
    private AuthDataAccess authDataAccess;

    private GameDataAccess gameDataAccess;
    private Connection databaseConnection;

    public Server() {
        try {
            DatabaseManager databaseManager = new DatabaseManager();
            databaseConnection = databaseManager.getConnection();

            // Create the database if it doesn't exist
            databaseManager.createDatabase();

            this.userDataAccess = new UserDAO(databaseConnection);
            this.authDataAccess = new AuthDAO(databaseConnection);
            this.gameDataAccess = new GameDAO(databaseConnection);

        } catch (DataAccessException e) {
            // Handle exceptions appropriately
            e.printStackTrace();
        }
    }


    public int run(int desiredPort) {
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
    }

    public void stop() {

        try {
            if (databaseConnection != null && !databaseConnection.isClosed()) {
                databaseConnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Spark.stop();
        Spark.awaitStop();
    }
}
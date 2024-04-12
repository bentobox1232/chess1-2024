package service;

import dataAccess.*;
import result.ClearResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ClearService {


    private final AuthDataAccess authDataAccess;
    private final GameDataAccess gameDataAccess;
    private final UserDataAccess userDataAccess;





    public ClearService(AuthDataAccess authDataAccess, GameDataAccess gameDataAccess, UserDataAccess userDataAccess) {
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
        this.userDataAccess = userDataAccess;
    }

    public ClearResult clear() {
        try {
            authDataAccess.clear();
            gameDataAccess.clear();
            userDataAccess.clear();

            return new ClearResult(200, true);
        } catch (DataAccessException e) {
            return new ClearResult(500, false, "Error: description");
        }

    }







}


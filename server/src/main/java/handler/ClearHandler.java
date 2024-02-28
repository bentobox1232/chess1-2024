package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import result.ClearResult;
import service.ClearService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;

public class ClearHandler implements Route {

    private final ClearService clearDataService;
    private final Gson gson;

    public ClearHandler(ClearService clearDataService, Gson gson) {
        this.clearDataService = clearDataService;
        this.gson = gson;
    }

    @Override
    public Object handle(Request request, Response response) {
        // Parse request if needed
        // Clear data using ClearDataService
        ClearService clearDataService = new ClearService();
        ClearResult clearDataResponse = clearDataService.clear();

        // Return success response
        response.status(200);
        return gson.toJson(clearDataResponse);
    }
}

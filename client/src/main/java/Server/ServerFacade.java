package Server;

import com.google.gson.Gson;
import request.*;
import result.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {
    public static String baseUrl = "http://localhost:";


    public static RegisterResult registerUser(String userName, String password, String email) throws IOException {
        String endpoint = baseUrl + "/user";

        RegisterRequest request = new RegisterRequest();
        request.setUsername(userName);
        request.setPassword(password);
        request.setEmail(email);

        return doPost(endpoint, request, RegisterResult.class);
    }

    public static LoginResult login(String username, String password) throws IOException {
        String endpoint = baseUrl + "/session";

        LoginRequest request = new LoginRequest();
        request.setPassword(password);
        request.setUsername(username);
        return doPost(endpoint, request, LoginResult.class);
    }

    public static LogoutResult logout(String authToken) throws IOException {
        String endpoint = baseUrl + "/session";

        LogoutRequest request = new LogoutRequest();
        request.setAuthToken(authToken);
        return doDelete(endpoint, authToken, LogoutResult.class);
    }

    public ClearResult clearDatabase() throws IOException {
        String endpoint = baseUrl + "/db";
        return doDelete(endpoint, null, ClearResult.class);
    }

    public static CreateResult createGame(String gameName, String authToken) throws Exception {
        String endpoint = baseUrl + "/game";

        CreateRequest request = new CreateRequest();
        request.setGameName(gameName);
        request.setAuthToken(authToken);

        CreateResult result = doPost(endpoint, request, CreateResult.class);

        return result;
    }

    public static ListResult listGames(String authToken) throws IOException {
        String endpoint = baseUrl + "/game";

        ListRequest request = new ListRequest(authToken);

        return doGet(endpoint, request, ListResult.class);
    }

    public static JoinResult joinGame(String authToken, Integer gameID, String color) throws IOException {
        String endpoint = baseUrl + "/game";
        JoinRequest request = new JoinRequest();
        request.setGameID(gameID);
        if (color != null) {
            request.setPlayerColor(color.toUpperCase());

        }
//        request.setPlayerColor(color.toUpperCase());
        request.setAuthToken(authToken);

        return doPut(endpoint, request, JoinResult.class);
    }

    // Implement other methods to interact with different endpoints
    public static <TRequest, TResult> TResult doPost(String endpoint, TRequest request, Class<TResult> resultClass) throws IOException {
        Gson gson = new Gson();
        String requestBody = gson.toJson(request);

        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();

        TResult result = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            result = gson.fromJson(response.toString(), resultClass);
        } catch (IOException e) {
            // If there's an error reading the response, leave result as null
        }

        connection.disconnect();
        return result;
    }

    public static <TRequest extends ListRequest, TResult> TResult doGet(String endpoint, TRequest request, Class<TResult> resultClass) throws IOException {
        // Create the URL object
        URL url = new URL(endpoint);

        // Open connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        if (request != null && request.getAuthToken() != null && !request.getAuthToken().isEmpty()) {
            connection.setRequestProperty("Authorization", request.getAuthToken());
        }

        int responseCode = connection.getResponseCode();
        TResult result = null;

        // Handle response
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine);
            }
            Gson gson = new Gson();
            result = gson.fromJson(response.toString(), resultClass);
        } catch (IOException e) {
            // If there's an error reading the response, leave result as null
        }

        connection.disconnect();
        return result;
    }

    public static <TResult> TResult doDelete(String endpoint, String authToken, Class<TResult> resultClass) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("DELETE");

        if (authToken != null && !authToken.isEmpty()) {
            // If authToken is provided, add it to the request headers
            connection.setRequestProperty("Authorization", authToken);
        }

        // Handle the response and parse it into the specified result class
        int responseCode = connection.getResponseCode();
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine);
            }
        }

        Gson gson = new Gson();
        TResult result = gson.fromJson(response.toString(), resultClass);

        connection.disconnect();

        return result;
    }
    public static <TRequest, TResult> TResult doPut(String endpoint, TRequest request, Class<TResult> resultClass) throws IOException {
        Gson gson = new Gson();
        String requestBody = gson.toJson(request);

        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = connection.getResponseCode();

        TResult result = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            result = gson.fromJson(response.toString(), resultClass);
        } catch (IOException e) {
            // If there's an error reading the response, leave result as null
        }

        connection.disconnect();
        return result;
    }
    private void handleResponse(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String responseLine;
                StringBuilder response = new StringBuilder();
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }
        } else {
            try (InputStream responseBody = connection.getErrorStream()) {
                // Handle error response body from InputStream ...
            }
        }
        connection.disconnect();
    }
}

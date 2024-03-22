package clientTests;

import Server.ServerFacade;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import request.ListRequest;
import result.*;
import server.Server;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeAll
    static void setUp() throws Exception {
        facade.clearDatabase();

       facade.registerUser("player1", "password", "p1@email.com");
    }


    @Test
    public void testRegisterUser_Success() throws IOException {
        RegisterResult result = facade.registerUser("testUser", "testPassword", "p1@email.com");
        assertTrue(result.isSuccess());
        assertTrue(result.getAuthToken().length() > 10);
    }

    @Test
    public void testLogin_Success() throws IOException {
        LoginResult result = facade.login("player1", "password");
        assertTrue(result.isSuccess());
        assertTrue(result.getAuthToken().length() > 10);
    }


    @Test
    public void logoutTest() throws IOException {
        var authData = facade.login("testUser", "testPassword");
        LogoutResult result = facade.logout(authData.getAuthToken());
        assertTrue(result.getSuccess());
    }

    @Test
    void createGame_Success() throws IOException {
        var authData = facade.login("testUser", "testPassword");
        // Create game using obtained auth token
        CreateResult createResult = facade.createGame("Test Game", authData.getAuthToken());
        assertTrue(createResult.isSuccess());
        assertTrue(createResult.getGameID() > 0);
    }
    @Test
    void listGames_Success() throws IOException {
        // Assuming your listGames method returns a ListResult object
        var authData = facade.login("testUser", "testPassword");

        // Create a ListRequest object
        ListRequest request = new ListRequest();
        request.setAuthToken(authData.getAuthToken());

        // Call the listGames method with the ListRequest
        ListResult result = facade.listGames(authData.getAuthToken());

        // Assert that the result is not null
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    void joinGameAsPlayer_Success() throws IOException {
        var authData = facade.login("testUser", "testPassword");
        var gameId = facade.createGame("newGame1", authData.getAuthToken());
        // Join the created game
        JoinResult result = facade.joinGame(authData.getAuthToken(), gameId.getGameID(), "BLACK");
        assertTrue(result.isSuccess());
    }

    @Test
    void joinGameAsObserver_Success() throws IOException {
        var authData = facade.login("testUser", "testPassword");
        var gameId = facade.createGame("newGame2", authData.getAuthToken());
        // Join the created game
        JoinResult result = facade.joinGame(authData.getAuthToken(), gameId.getGameID(), null);
        assertTrue(result.isSuccess());
    }

//    @Test
//    public void testRegisterUser_Failure() throws IOException {
//        // Attempt to register with existing username
//        RegisterResult result = facade.registerUser("player1", "testPassword", "p1@email.com");
//        assertFalse(result.isSuccess());
//        assertNull(result.getAuthToken());
//    }
//
//    @Test
//    public void testLogin_Failure() throws IOException {
//        // Attempt to login with incorrect password
//        LoginResult result = facade.login("player1", "wrongPassword");
//        assertFalse(result.isSuccess());
//        assertNull(result.getAuthToken());
//    }
//
//    @Test
//    public void logoutTest_Failure() throws IOException {
//        // Attempt to logout with invalid or expired authentication token
//        LogoutResult result = facade.logout("invalidAuthToken");
//        assertFalse(result.getSuccess());
//    }
//
//    @Test
//    void createGame_Failure() throws IOException {
//        // Attempt to create a game with invalid authentication token
//        CreateResult createResult = facade.createGame("Test Game", "invalidAuthToken");
//        assertFalse(createResult.isSuccess());
//        assertNull(createResult.getGameID());
//    }
//
//    @Test
//    void listGames_Failure() throws IOException {
//        // Attempt to list games with invalid authentication token
//        ListResult result = facade.listGames("invalidAuthToken");
//        assertFalse(result.isSuccess());
//    }
//
//    @Test
//    void joinGameAsPlayer_Failure() throws IOException {
//        // Attempt to join a game with invalid authentication token
//        JoinResult result = facade.joinGame("invalidAuthToken", 123456, "BLACK");
//        assertFalse(result.isSuccess());
//    }
//
//    @Test
//    void joinGameAsObserver_Failure() throws IOException {
//        // Attempt to join a game as an observer with invalid authentication token
//        JoinResult result = facade.joinGame("invalidAuthToken", 123456, null);
//        assertFalse(result.isSuccess());
//    }

}

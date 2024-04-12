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

       ServerFacade.registerUser("player1", "password", "p1@email.com");
    }

    @AfterAll
    public static void tearDown() throws IOException {
        facade.clearDatabase();
    }


    @Test
    public void testRegisterUser_Success() throws IOException {
        RegisterResult result = ServerFacade.registerUser("testUser", "testPassword", "p1@email.com");
        assertTrue(result.isSuccess());
        assertTrue(result.getAuthToken().length() > 10);
    }

    @Test
    public void testLogin_Success() throws IOException {
        LoginResult result = ServerFacade.login("player1", "password");
        assertTrue(result.isSuccess());
        assertTrue(result.getAuthToken().length() > 10);
    }


    @Test
    public void logoutTest() throws IOException {
        var authData = ServerFacade.login("testUser", "testPassword");
        LogoutResult result = ServerFacade.logout(authData.getAuthToken());
        assertTrue(result.getSuccess());
    }

    @Test
    void createGame_Success() throws Exception {
        var authData = ServerFacade.login("testUser", "testPassword");
        // Create game using obtained auth token
        CreateResult createResult = ServerFacade.createGame("Test Game", authData.getAuthToken());
        assertTrue(createResult.isSuccess());
        assertTrue(createResult.getGameID() > 0);
    }
    @Test
    void listGames_Success() throws IOException {
        // Assuming your listGames method returns a ListResult object
        var authData = ServerFacade.login("testUser", "testPassword");

        // Create a ListRequest object
        ListRequest request = new ListRequest();
        request.setAuthToken(authData.getAuthToken());

        // Call the listGames method with the ListRequest
        ListResult result = ServerFacade.listGames(authData.getAuthToken());

        // Assert that the result is not null
        assertNotNull(result);
        assertTrue(result.isSuccess());
    }

    @Test
    void joinGameAsPlayer_Success() throws Exception {
        var authData = ServerFacade.login("testUser", "testPassword");
        var gameId = ServerFacade.createGame("newGame1", authData.getAuthToken());
        // Join the created game
        JoinResult result = ServerFacade.joinGame(authData.getAuthToken(), gameId.getGameID(), "BLACK");
        assertTrue(result.isSuccess());
    }

    @Test
    void joinGameAsObserver_Success() throws Exception {
        var authData = ServerFacade.login("testUser", "testPassword");
        var gameId = ServerFacade.createGame("newGame2", authData.getAuthToken());
        // Join the created game
        JoinResult result = ServerFacade.joinGame(authData.getAuthToken(), gameId.getGameID(), null);
        assertTrue(result.isSuccess());
    }

    @Test
    public void testRegisterUser_Failure() throws IOException {
        // Attempt to register with existing username
        RegisterResult result = ServerFacade.registerUser("player1", "testPassword", "p1@email.com");
        assertNull(result);
    }

    @Test
    public void testLogin_Failure() throws IOException {
        // Attempt to login with incorrect password
        LoginResult result = ServerFacade.login("player1", "wrongPassword");
        assertNull(result);
    }

    @Test
    public void logoutTest_Failure() throws IOException {
        IOException exception = assertThrows(IOException.class, () -> {
            ServerFacade.logout("invalidAuthToken");
        });
        assertNotNull(exception);
    }

    @Test
    void createGame_Failure() throws Exception {
        // Attempt to create a game with invalid authentication token
        CreateResult createResult = ServerFacade.createGame("Test Game", "invalidAuthToken");
        assertNull(createResult);
    }

    @Test
    void listGames_Failure() throws IOException {
        // Attempt to list games with invalid authentication token
        ListResult result = ServerFacade.listGames("invalidAuthToken");
        assertNull(result);
    }

    @Test
    void joinGameAsPlayer_Failure() throws IOException {
        // Attempt to join a game with invalid authentication token
        JoinResult result = ServerFacade.joinGame("invalidAuthToken", 123456, "BLACK");
        assertNull(result);
    }

    @Test
    void joinGameAsObserver_Failure() throws IOException {
        // Attempt to join a game as an observer with invalid authentication token
        JoinResult result = ServerFacade.joinGame("invalidAuthToken", 123456, null);
        assertNull(result);
    }

}

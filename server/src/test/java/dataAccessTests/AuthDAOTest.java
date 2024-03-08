package dataAccessTests;

import dao.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.AuthData;
import org.junit.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthDAOTest {

    private static Connection connection;
    private AuthDAO authDAO;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        connection = DatabaseManager.getConnection();
    }

    @Before
    public void setUp() throws Exception {
        authDAO = new AuthDAO(connection);
    }

    @After
    public void tearDown() throws Exception {
        authDAO.clear();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        connection.close();
    }

    @Test
    public void testCreateAuth_Positive() {
        AuthData authData = new AuthData("testAuthToken", "testUserId");

        assertDoesNotThrow(() -> authDAO.createAuth(authData));

    }

    @Test
    public void testCreateAuth_Negative_DuplicateAuthToken() {
        AuthData authData1 = new AuthData("duplicateAuthToken", "user1");
        AuthData authData2 = new AuthData("duplicateAuthToken", "user2");

        assertDoesNotThrow(() -> authDAO.createAuth(authData1));

        assertThrows(DataAccessException.class, () -> authDAO.createAuth(authData2));
    }

    @Test
    public void testGetAuth_Positive() throws DataAccessException {
        String authToken = "existingAuthToken";
        AuthData authData = new AuthData(authToken, "username");
        authDAO.createAuth(authData);

        AuthData retrievedAuth = authDAO.getAuth(authToken);

        assertNotNull(retrievedAuth);
        assertEquals(authToken, retrievedAuth.getAuthToken());
    }

    @Test
    public void testGetAuth_Negative_NonExistentAuthToken() throws DataAccessException {
        String authToken = "nonExistentAuthToken";

        AuthData retrievedAuth = authDAO.getAuth(authToken);

        assertNull(retrievedAuth);
    }

    @Test
    public void testDeleteAuth_Positive() throws DataAccessException {
        String authToken = "authTokenToDelete";

        AuthData authData = new AuthData(authToken, "userToDelete");
        authDAO.createAuth(authData);

        assertDoesNotThrow(() -> authDAO.deleteAuth(authToken));

        AuthData retrievedAuth = authDAO.getAuth(authToken);

        assertNull(retrievedAuth);
    }

    @Test
    public void testDeleteAuth_Negative_NonExistentAuthToken() {
        String authToken = "nonExistentAuthToken";

        assertDoesNotThrow(() -> authDAO.deleteAuth(authToken));
    }
}


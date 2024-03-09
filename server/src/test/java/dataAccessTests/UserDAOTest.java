package dataAccessTests;

import static org.junit.jupiter.api.Assertions.*;

import dao.UserDAO;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.UserData;
import org.junit.jupiter.api.*;

import java.sql.Connection;

public class UserDAOTest {

    private static Connection connection;
    private UserDAO userDAO;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        connection = DatabaseManager.getConnection();
    }

    @BeforeEach
    public void setUp() throws Exception {
        userDAO = new UserDAO(connection);
    }

    @AfterEach
    public void tearDown() throws Exception {
        userDAO.clear();
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
        connection.close();
    }

    @Test
    public void testIsUsernameTaken_Positive() throws DataAccessException {

        String username = "existingUser";
        userDAO.createUser(new UserData(username, "password123", "user@example.com"));

        boolean isTaken = userDAO.isUsernameTaken(username);

        assertTrue(isTaken);
    }

    @Test
    public void testIsUsernameTaken_Negative() throws DataAccessException {

        String username = "nonExistingUser";

        boolean isTaken = userDAO.isUsernameTaken(username);

        assertFalse(isTaken);
    }

    @Test
    public void testCreateUser_Positive() throws DataAccessException {

        UserData userData = new UserData("newUser", "password456", "newuser@example.com");


        userDAO.createUser(userData);


        assertTrue(userDAO.isUsernameTaken(userData.getUsername()));
    }

    @Test
    public void testCreateUser_Negative() throws DataAccessException {

        UserData existingUserData = new UserData("existingUser", "password123", "existinguser@example.com");

        assertThrows(DataAccessException.class, () -> {
            userDAO.createUser(existingUserData);
            userDAO.createUser(existingUserData);
        });
    }

    @Test
    public void testIsCorrectLoginInfo_Positive() throws DataAccessException {

        String username = "existingUserRandomm";
        String password = "password123432";
        userDAO.createUser(new UserData(username, password, "user@example.com"));


        boolean isCorrect = userDAO.isCorrectLoginInfo(username, password);


        assertTrue(isCorrect);
    }

    @Test
    public void testIsCorrectLoginInfo_Negative() throws DataAccessException {

        String username = "existingUser";
        String password = "incorrectPassword";
        userDAO.createUser(new UserData(username, "password123", "user@example.com"));


        boolean isCorrect = userDAO.isCorrectLoginInfo(username, password);


        assertFalse(isCorrect);
    }

    @Test
    public void testGetUserByUsername_Positive() throws DataAccessException {

        String username = "existingUser";
        String password = "password123";
        String email = "user@example.com";
        userDAO.createUser(new UserData(username, password, email));


        UserData retrievedUser = userDAO.getUserByUsername(username);


        assertNotNull(retrievedUser);
        assertEquals(username, retrievedUser.getUsername());
        assertEquals(password, retrievedUser.getPassword());
        assertEquals(email, retrievedUser.getEmail());
    }

    @Test
    public void testGetUserByUsername_Negative() throws DataAccessException {

        String username = "nonExistingUser";


        UserData retrievedUser = userDAO.getUserByUsername(username);


        assertNull(retrievedUser);
    }
}


package dataAccessTests;

import chess.*;
import dao.GameDAO;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.GameData;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.List;

public class GameDAOTest {

    private static Connection connection;
    private GameDAO gameDAO;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        connection = DatabaseManager.getConnection();
    }

    @Before
    public void setUp() throws Exception {
        gameDAO = new GameDAO(connection);
    }

    @After
    public void tearDown() throws Exception {
        gameDAO.clear();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        connection.close();
    }

    @Test
    public void testCreateGame_Positive() throws DataAccessException {
        GameData gameData = new GameData(1, "Player1", "Player2", "ChessGame1", new ChessGame());
        try {
            gameDAO.createGame(gameData);
            assertNotNull(gameData.getGameID()); // Ensure the ID is assigned
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateGame_Negative() {
        GameData gameData1 = new GameData(1, "Player1", "Player2", "ChessGame1", new ChessGame());
        GameData gameData2 = new GameData(2, "Player1", "Player2", "ChessGame1", new ChessGame());

        try {
            gameDAO.createGame(gameData1);
            gameDAO.createGame(gameData2); // This should fail

            fail("Expected DataAccessException, but no exception was thrown");
        } catch (DataAccessException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetGameByID_Positive() throws DataAccessException {
        GameData gameData = new GameData(1, "Player1", "Player2", "ChessGame1", new ChessGame());
        gameDAO.createGame(gameData);

        try {
            GameData retrievedGame = gameDAO.getGameByID(gameData.getGameID());
            assertNotNull(retrievedGame);
            assertEquals(gameData.getGameID(), retrievedGame.getGameID());
            assertEquals(gameData.getWhiteUsername(), retrievedGame.getWhiteUsername());
            assertEquals(gameData.getBlackUsername(), retrievedGame.getBlackUsername());
            assertEquals(gameData.getGameName(), retrievedGame.getGameName());
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetGameByID_Negative() {
        try {
            GameData retrievedGame = gameDAO.getGameByID(9999); // Non-existent ID
            assertNull(retrievedGame);
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateGame_Positive() throws DataAccessException, InvalidMoveException {
        GameData gameData = new GameData(1, "Player1", "Player2", "ChessGame1", new ChessGame());
        gameDAO.createGame(gameData);

        gameData.setWhiteUsername("NewPlayer1");
        gameData.setBlackUsername("NewPlayer2");
        gameData.setGameName("NewChessGame");

        ChessPosition startPosition = new ChessPosition(4, 6); // Example position
        ChessPosition endPosition = new ChessPosition(4, 4); // Example position

        gameData.getGame().getBoard().addPiece(startPosition, new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));

        ChessPiece pieceAtStartPosition = gameData.getGame().getBoard().getPiece(startPosition);
        assertNotNull(pieceAtStartPosition);

        ChessMove chessMove = new ChessMove(startPosition, endPosition, null); // No promotion for this test
        gameData.getGame().makeMove(chessMove);

        try {
            gameDAO.updateGame(gameData);

            GameData updatedGame = gameDAO.getGameByID(gameData.getGameID());

            assertNotNull(updatedGame);
            assertEquals(gameData.getWhiteUsername(), updatedGame.getWhiteUsername());
            assertEquals(gameData.getBlackUsername(), updatedGame.getBlackUsername());
            assertEquals(gameData.getGameName(), updatedGame.getGameName());
            assertEquals(gameData.getGame().getBoard(), updatedGame.getGame().getBoard());
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }


    @Test
    public void testUpdateGame_Negative() {
        GameData gameData = new GameData(1000,"Player1", "Player2", "ChessGame1", new ChessGame());

        try {
            gameDAO.updateGame(gameData);

            fail("Expected DataAccessException, but no exception was thrown");
        } catch (DataAccessException e) {
        }
    }

    @Test
    public void testGetListGame_Positive() throws DataAccessException {
        GameData game1 = new GameData(1, "Player1", "Player2", "ChessGame1", new ChessGame());
        GameData game2 = new GameData(2,"Player3", "Player4", "ChessGame2", new ChessGame());
        gameDAO.createGame(game1);
        gameDAO.createGame(game2);

        try {
            List<GameData> gameList = gameDAO.getListGame();

            assertNotNull(gameList);
            assertEquals(2, gameList.size());

            assertEquals(gameList.get(0).getGameID(), game1.getGameID());
            assertEquals(gameList.get(1).getGameID(), game2.getGameID());
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetListGame_Negative() {
        try {
            List<GameData> gameList = gameDAO.getListGame();
            assertNotNull(gameList);
            assertTrue(gameList.isEmpty());
        } catch (DataAccessException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}


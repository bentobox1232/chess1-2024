package dataAccess;

import model.GameData;
import model.UserData;

import java.util.List;

public interface GameDataAccess {
    void createGame(GameData gameData) throws DataAccessException;
    void updateGame(GameData gameData) throws DataAccessException;

    List<GameData> getListGame() throws DataAccessException;
    GameData getGameByID(Integer gameID) throws DataAccessException;
    void clear() throws DataAccessException;
}

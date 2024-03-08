package dataAccess;

import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDataAccess implements GameDataAccess{

    private final Map<Integer, GameData> gameDataMap;

    public MemoryGameDataAccess() {
        this.gameDataMap = new HashMap<>();
    }
    @Override
    public void createGame(GameData gameData) {
        gameDataMap.put(gameData.getGameID(), gameData);
    }

    @Override
    public void updateGame(GameData gameData) {
        if(gameDataMap.containsKey(gameData.getGameID())) {
            gameDataMap.put(gameData.getGameID(), gameData);
        }
    }

    @Override
    public List<GameData> getListGame() {
        List<GameData> games = new ArrayList<>();
        for(Integer key: gameDataMap.keySet()){
            games.add(gameDataMap.get(key));
        }
        return games;
    }

    @Override
    public GameData getGameByID(Integer gameID) {
        return gameDataMap.get(gameID);
    }

    @Override
    public void clear() throws DataAccessException{
        gameDataMap.clear();
    }


}

package result;

import model.GameData;

import java.util.List;

public class ListResult {
    int errorCode;
    boolean success;
    String message;
    List<GameData> games;

    public ListResult(int errorCode, boolean success, List<GameData> games) {
        this.errorCode = errorCode;
        this.games = games;
        this.success = success;
    }

    public ListResult(int errorCode, boolean success, String message) {
        this.errorCode = errorCode;
        this.message = message;
        this.success = success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public List<GameData> getGames() {
        return games;
    }

    public boolean isSuccess() {
        return success;
    }

}

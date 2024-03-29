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

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<GameData> getGames() {
        return games;
    }

    public void setGames(List<GameData> games) {
        this.games = games;
    }
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

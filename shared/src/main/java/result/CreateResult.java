package result;

public class CreateResult {
    private boolean success;
    private String message;
    private int errorCode;
    private Integer gameID;

    public CreateResult(int errorCode, Integer gameID) {
        this.errorCode = errorCode;
        this.gameID = gameID;
    }

    public CreateResult(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}

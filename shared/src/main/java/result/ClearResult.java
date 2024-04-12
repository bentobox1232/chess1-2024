package result;

public class ClearResult {
    private String message;
    private int errorCode;

    private Boolean success;

    public ClearResult(int errorCode, boolean success) {
        this.errorCode = errorCode;
        this.success = success;
    }

    public ClearResult(int errorCode, boolean success, String message) {
        this.message = message;
        this.success = success;
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }


}

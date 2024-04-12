package result;

public class JoinResult {
    int errorCode;
    String message;
    boolean success;

    public JoinResult(int errorCode, boolean success) {
        this.errorCode = errorCode;
        this.success = success;
    }

    public JoinResult(int errorCode, boolean success, String message) {
        this.errorCode = errorCode;
        this.message = message;
        this.success = success;
    }
    public int getErrorCode() {
        return errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

}

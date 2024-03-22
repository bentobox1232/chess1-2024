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

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

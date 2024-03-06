package result;

public class JoinResult {
    int errorCode;
    String message;

    public JoinResult(int errorCode) {
        this.errorCode = errorCode;
    }

    public JoinResult(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
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
}

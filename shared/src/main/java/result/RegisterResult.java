package result;

public class RegisterResult {

    private boolean success;
    private String message;
    private int errorCode;

    private String authToken;

    private String username;

    public RegisterResult(int errorCode, String userName, String authToken) {
        this.errorCode = errorCode;
        this.username = userName;
        this.authToken = authToken;
    }

    public RegisterResult(int errorCode, String message) {
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
}

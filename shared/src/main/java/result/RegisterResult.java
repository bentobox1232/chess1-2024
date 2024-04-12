package result;

public class RegisterResult {

    private boolean success;
    private String message;
    private int errorCode;

    public String getAuthToken() {
        return authToken;
    }

    private String authToken;

    public RegisterResult(int errorCode, boolean success, String authToken) {
        this.errorCode = errorCode;
        this.success = success;
        this.authToken = authToken;
    }

    public RegisterResult(int errorCode, boolean success, String message) {
        this.errorCode = errorCode;
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getErrorCode() {
        return errorCode;
    }

}

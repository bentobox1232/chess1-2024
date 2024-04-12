package result;

public class LogoutResult {
    private String message;
    private int errorCode;
    private Boolean success;

    public LogoutResult(int errorCode, boolean success) {
        this.errorCode = errorCode;
        this.success = success;
    }
    public LogoutResult(int errorCode, boolean success, String message){
        this.errorCode = errorCode;
        this.success = success;
        this.message = message;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public Boolean getSuccess() {
        return success;
    }

}

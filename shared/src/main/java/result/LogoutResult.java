package result;

public class LogoutResult {
    private String message;
    private int errorCode;
    private Boolean success;

    public LogoutResult(int errorCode) {
        this.errorCode = errorCode;
    }
    public LogoutResult(int errorCode, String message){
        this.errorCode = errorCode;
        this.message = message;
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

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}

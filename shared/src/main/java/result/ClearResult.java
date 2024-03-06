package result;

public class ClearResult {
    private String message;
    private int errorCode;

    private Boolean success;

    public ClearResult(){}

    public ClearResult(int errorCode) {
        this.errorCode = errorCode;
    }

    public ClearResult(int errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
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

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }


}

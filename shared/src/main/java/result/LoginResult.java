package result;

import model.AuthData;

import java.util.Objects;

public class LoginResult {
    private String authToken;
    private String username;
    private Boolean success;
    private String message;
    private int errorCode;

    public LoginResult(int errorCode, String userName, String authToken) {
        this.errorCode = errorCode;
        this.username = userName;
        this.authToken = authToken;
    }

    public LoginResult(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginResult that = (LoginResult) o;
        return Objects.equals(authToken, that.authToken) && Objects.equals(username, that.username) && Objects.equals(success, that.success) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, username, success, message);
    }
}

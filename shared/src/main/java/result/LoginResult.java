package result;

import java.util.Objects;

public class LoginResult {
    private String authToken;
    private String username;
    private Boolean success;
    private String message;
    private int errorCode;

    public LoginResult(int errorCode, boolean success, String userName, String authToken) {
        this.errorCode = errorCode;
        this.success = success;
        this.username = userName;
        this.authToken = authToken;
    }

    public LoginResult(int errorCode, boolean success, String message) {
        this.errorCode = errorCode;
        this.success = success;
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public Boolean isSuccess() {
        return success;
    }

    public int getErrorCode() {
        return errorCode;
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

package model;

import java.util.Objects;
import java.util.UUID;

public class AuthData {
    private String authToken;
    private String username;

    public AuthData() {
        authToken = UUID.randomUUID().toString();
        username = new String();
    }

    public AuthData (String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public AuthData(String username) {
        this.username = username;
        authToken = UUID.randomUUID().toString();
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

    @Override
    public int hashCode() {
        return Objects.hash(authToken, username);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AuthData authData = (AuthData) obj;

        return username.equals(authData.getUsername()) && authToken.equals(authData.getAuthToken());

    }
}

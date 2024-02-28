package model;

import java.util.Objects;

public class UserData {
    private String username;
    private String password;
    private String email;

    public UserData() {
        username = new String();
        password = new String();
        email = new String();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UserData userData = (UserData) obj;

        return username.equals(userData.getUsername()) && password.equals(userData.getPassword()) && email.equals(userData.getEmail());
    }
}

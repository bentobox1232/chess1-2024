package request;

public class ListRequest {
    private String authToken;

    public ListRequest() {
    }

    public ListRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}

package request;

public class JoinRequest {
    String playerColor;
    Integer gameID;

    public JoinRequest(String playerColor, Integer gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}

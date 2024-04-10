package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    private final ChessGame.TeamColor playerColor;

    public JoinPlayer(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
        super(CommandType.JOIN_PLAYER, authToken, gameID);
        this.playerColor = playerColor;
    }
}
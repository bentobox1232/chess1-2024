package Displays;

import WebSocket.GameHandler;
import WebSocket.WebSocketFacade;
import chess.*;
import com.google.gson.Gson;
import webSocketMessages.userCommands.*;

import java.util.Collection;

import static org.glassfish.grizzly.Interceptor.RESET;
import static ui.EscapeSequences.*;

public class Gameplay extends Display implements GameHandler {
    private final WebSocketFacade webSocketFacade;
    private final Gson gson;
    private final String authToken;
    private final Integer gameID;
    private ChessGame game;
    private final ChessGame.TeamColor playerColor;
    private final ChessBoard board;
    private String[][] colors = new String[7][7];

    String[][] pieces = new String[7][7];

    public Gameplay(String authToken, Integer gameID, String color) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.gson = new Gson();
        this.webSocketFacade = new WebSocketFacade(this);

        if (color != null) {
            this.playerColor = parseTeamColor(color);
            joinGame();
        } else {
            this.playerColor = null;
            joinObserver();
        }

        this.game = new ChessGame();
        this.board = (boardColors, boardPieces) -> printBoard(boardColors, boardPieces);
    }



    @Override
    protected Boolean evaluate(String[] parsedInput) {
        switch (parsedInput[0]) {
            case "help":
                printHelpMessage();
                break;
            case "r":
                board.getBoard();
                break;
            case "leave":
                leaveGame();
                return true;
            case "m":
                makeMove(parsedInput);
                break;
            case "resign":
                resignGame();
                return true;
            case "h":
                highlightMoves(parsedInput);
                break;
            default:
                System.out.println("Invalid command. Type 'help' for more information.");
        }
        return false;
    }

    private ChessPosition parsePosition(String input) {
        if (input.length() != 2) {
            System.out.println("Invalid position format. Please use the format 'LetterNumber', e.g., 'a1'.");
            return null;
        }

        char fileChar = input.charAt(0);
        char rankChar = input.charAt(1);

        if (fileChar < 'a' || fileChar > 'h' || rankChar < '1' || rankChar > '8') {
            System.out.println("Invalid position. File must be between 'a' and 'h', and rank must be between '1' and '8'.");
            return null;
        }

        int row = 8 - (rankChar - '1');
        int col = fileChar - 'a' + 1;

        return new ChessPosition(row, col);
    }


    private void highlightMoves(String[] parsedInput) {
        if (parsedInput.length != 2) {
            System.out.println("Invalid command format. Use 'h <position>'.");
            return;
        }

        ChessPosition position = parsePosition(parsedInput[1]);
        if (position == null) {
            System.out.println("Invalid position format. Use 'h <position>'.");
            return;
        }

        if (playerColor == null || playerColor == ChessGame.TeamColor.WHITE) {
            System.out.println("Observers cannot highlight moves on the board.");
            return;
        }

        Collection<ChessMove> potentialMoves = game.validMoves(position);

        if (potentialMoves.isEmpty()) {
            System.out.println("No valid moves for the piece at " + position.toString() + ".");
            return;
        }

        String[][] colors = new String[7][7];

        for (ChessMove move : potentialMoves) {
            int rowModifier = move.getEndPosition().getRow() - 1;
            int colModifier = move.getEndPosition().getColumn() - 1;
            if (playerColor != ChessGame.TeamColor.BLACK) {
                rowModifier = move.getEndPosition().getRow() - 1;
            } else {
                colModifier = move.getEndPosition().getColumn();
            }

            String color = colors[rowModifier][colModifier];
            if (color.equals(SET_BG_COLOR_WHITE)) {
                colors[rowModifier][colModifier] = SET_BG_COLOR_GREEN;
            } else if (color.equals(SET_BG_COLOR_LIGHT_GREY)) {
                colors[rowModifier][colModifier] = SET_BG_COLOR_DARK_GREEN;
            }
        }


        printBoard(colors, pieces);

        // Reset colors after highlighting
        resetColors();
    }

    private void resetColors() {
        this.colors = new String[][]{
                {SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY},
                {SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE},
                {SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY},
                {SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE},
                {SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY},
                {SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE},
                {SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY},
                {SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE}
        };
    }


    private void printHelpMessage() {
        System.out.println("Available commands:");
        System.out.println("help\t\t\tDisplay this help message");
        System.out.println("r\t\t\tRefresh the board");
        System.out.println("leave\t\t\tLeave the current game");
        System.out.println("m <start> <end>\tMake a move from start to end (e.g., m e2 e4)");
        System.out.println("resign\t\t\tResign the game");
        System.out.println("h <position>\t\tHighlight valid moves for the piece at the specified position (e.g., h e2)");
    }

    private ChessGame.TeamColor parseTeamColor(String color) {
        return color.equalsIgnoreCase("black") ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
    }

    private void joinGame() {
        JoinPlayer joinPlayer = new JoinPlayer(authToken, gameID, playerColor);
        String jsonString = gson.toJson(joinPlayer);
        webSocketFacade.sendMessage(jsonString);
    }

    private void joinObserver() {
        JoinObserver joinObserver = new JoinObserver(authToken, gameID);
        String jsonString = gson.toJson(joinObserver);
        webSocketFacade.sendMessage(jsonString);
    }

    private void leaveGame() {
        Leave leave = new Leave(authToken, gameID);
        String leaveString = gson.toJson(leave);
        webSocketFacade.sendMessage(leaveString);
        webSocketFacade.disconnect();
    }

    private void makeMove(String[] parsedInput) {
        if (playerColor == null || playerColor != ChessGame.TeamColor.WHITE) {
            System.out.println("Observers cannot move pieces on the board.");
            return;
        }

        ChessPosition startPosition = parsePosition(parsedInput[1]);
        ChessPosition endPosition = parsePosition(parsedInput[2]);

        if (startPosition == null || endPosition == null) {
            System.out.println("Invalid move format. Use 'm <start> <end>'.");
            return;
        }

        MakeMove makeMove = new MakeMove(authToken, gameID, new ChessMove(startPosition, endPosition, null));
        String makeMoveString = gson.toJson(makeMove);
        webSocketFacade.sendMessage(makeMoveString);
    }

    private void resignGame() {
        Resign resign = new Resign(authToken, gameID);
        String resignString = gson.toJson(resign);
        webSocketFacade.sendMessage(resignString);
        webSocketFacade.disconnect();
    }

    private void highlightBoard(ChessPosition startPosition) {
        if (playerColor != null && playerColor == ChessGame.TeamColor.WHITE && game.getBoard().chessBoard[startPosition.getRow()][startPosition.getColumn()] == null) {
            System.out.println(SET_TEXT_COLOR_LIGHT_GREY + "No piece at specified location.");
            return;
        }

        ChessPosition actualPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);
        Collection<ChessMove> potentialMoves = game.validMoves(actualPosition);

        if (potentialMoves.isEmpty()) {
            System.out.println(SET_TEXT_COLOR_LIGHT_GREY + "No valid moves.");
            return;
        }

        for (ChessMove move : potentialMoves) {
            int rowModifier = move.getEndPosition().getRow() - 1;
            int colModifier = move.getEndPosition().getColumn() - 1;
            if (playerColor != ChessGame.TeamColor.BLACK) {
                rowModifier = move.getEndPosition().getRow() - 1;
            } else {
                colModifier = move.getEndPosition().getColumn();
            }

            String color = colors[rowModifier][colModifier];
            if (color.equals(SET_BG_COLOR_WHITE)) {
                colors[rowModifier][colModifier] = SET_BG_COLOR_GREEN;
            } else if (color.equals(SET_BG_COLOR_LIGHT_GREY)) {
                colors[rowModifier][colModifier] = SET_BG_COLOR_DARK_GREEN;
            }
        }

        printBoard(colors, pieces);

        this.colors = new String[][]{
                {SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY},
                {SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE},
                {SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY},
                {SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE},
                {SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY},
                {SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE},
                {SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY},
                {SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE}
        };
    }

    private ChessPosition getPosition(String coordinates) {
        int row = extractNumber(coordinates);
        int col = 0;

        switch (Character.toLowerCase(coordinates.charAt(0))) {
            case 'a' -> col = 1;
            case 'b' -> col = 2;
            case 'c' -> col = 3;
            case 'd' -> col = 4;
            case 'e' -> col = 5;
            case 'f' -> col = 6;
            case 'g' -> col = 7;
            case 'h' -> col = 8;
        }

        return new ChessPosition(row, col);
    }

    private int extractNumber(String input) {
        // Regular expression to match the number
        String regex = "\\d+";

        // Using Pattern and Matcher to find the number
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(input);

        // If a number is found, parse it and return
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }

        // If no number found, return a default value (you can handle this differently based on your requirements)
        return -1; // or throw an exception, or return a default value, etc.
    }


    @Override
    public void updateGame(ChessGame game) {
        this.game = game;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String piece = EMPTY;

                if (game.getBoard().chessBoard[i][j] != null) {
                    boolean pieceIsWhite = game.getBoard().chessBoard[i][j].getTeamColor() == ChessGame.TeamColor.WHITE;
                    switch (game.getBoard().chessBoard[i][j].getPieceType()) {
                        case PAWN -> piece = pieceIsWhite ? WHITE_PAWN : BLACK_PAWN;
                        case KNIGHT -> piece = pieceIsWhite ? WHITE_KNIGHT : BLACK_KNIGHT;
                        case ROOK -> piece = pieceIsWhite ? WHITE_ROOK : BLACK_ROOK;
                        case BISHOP -> piece = pieceIsWhite ? WHITE_BISHOP : BLACK_BISHOP;
                        case QUEEN -> piece = pieceIsWhite ? WHITE_QUEEN : BLACK_QUEEN;
                        case KING -> piece = pieceIsWhite ? WHITE_KING : BLACK_KING;
                    }
                }

                this.pieces[i][j] = piece;
            }
        }

        System.out.println(ERASE_SCREEN);
        printBoard(colors, pieces);
    }

    @Override
    public void printMessage(String message) {
        System.out.println(ERASE_SCREEN);
        printBoard(colors, pieces);
        System.out.println(SET_TEXT_COLOR_LIGHT_GREY + message);
    }

    private void printBoard(String[][] boardColors, String[][] boardPieces) {
        StringBuilder horizontalLine = new StringBuilder("  ---------------------------------");
        System.out.println(horizontalLine);
        for (int i = 0; i < 8; i++) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    System.out.print(SET_TEXT_COLOR_BLACK + boardColors[i][j] + boardPieces[i][j] + RESET + " ");
                } else {
                    System.out.print(SET_TEXT_COLOR_WHITE + boardColors[i][j] + boardPieces[i][j] + RESET + " ");
                }
            }
            System.out.println(SET_TEXT_COLOR_WHITE + "|" + RESET);
            System.out.println(horizontalLine);
        }
        System.out.println("    a   b   c   d   e   f   g   h");
    }

}

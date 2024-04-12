package REPLS;

import WebSocket.GameHandler;
import WebSocket.WebSocketFacade;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import webSocketMessages.userCommands.*;

import java.util.Collection;

import static ui.EscapeSequences.*;

public class GameplayUI extends REPL implements GameHandler {

    WebSocketFacade wsf = new WebSocketFacade(this);
    String authToken;
    Integer gameID;

    ChessGame game;
    ChessGame.TeamColor playerColor;
    Gson gson = new Gson();
    BoardGenerator board;

    String[][] colors = {
            {SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY},
            {SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE},
            {SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY},
            {SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE},
            {SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY},
            {SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE},
            {SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY},
            {SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE, SET_BG_COLOR_LIGHT_GREY, SET_BG_COLOR_WHITE}
    };

    String[][] pieces = {
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY}
    };


    public GameplayUI(String authToken, Integer gameID, String color) {

        this.authToken = authToken;
        this.gameID = gameID;

        if (color != null) {

            if(color.equalsIgnoreCase("black")){
                this.playerColor = ChessGame.TeamColor.BLACK;
            } else if (color.equalsIgnoreCase("white")) {
                this.playerColor = ChessGame.TeamColor.WHITE;
            }

            JoinPlayer joinPlayer = new JoinPlayer(this.authToken, this.gameID, this.playerColor);
            String jsonString = this.gson.toJson(joinPlayer);
            this.wsf.sendMessage(jsonString);
        } else {
            this.playerColor = null;

            JoinObserver joinObserver = new JoinObserver(this.authToken, this.gameID);
            String jsonString = this.gson.toJson(joinObserver);
            this.wsf.sendMessage(jsonString);
// need to do the observer class

        }



        if (this.playerColor == null || !this.playerColor.equals(ChessGame.TeamColor.WHITE)) {

            this.board = (boardColors, boardPieces) -> {

                String string = SET_TEXT_BOLD + SET_BG_COLOR_BLACK + "\n\t" + SET_TEXT_COLOR_BLACK +
                        SET_BG_COLOR_LIGHT_GREY + "    H  G  F  E  D  C  B  A    " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + " 1 " + boardColors[0][7] + boardPieces[0][7] + boardColors[0][6] + boardPieces[0][6] + boardColors[0][5] + boardPieces[0][5] + boardColors[0][4] + boardPieces[0][4] + boardColors[0][3] + boardPieces[0][3] + boardColors[0][2] + boardPieces[0][2] + boardColors[0][1] + boardPieces[0][1] + boardColors[0][0] + boardPieces[0][0] + SET_BG_COLOR_LIGHT_GREY + " 1 " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + " 2 " + boardColors[1][7] + boardPieces[1][7] + boardColors[1][6] + boardPieces[1][6] + boardColors[1][5] + boardPieces[1][5] + boardColors[1][4] + boardPieces[1][4] + boardColors[1][3] + boardPieces[1][3] + boardColors[1][2] + boardPieces[1][2] + boardColors[1][1] + boardPieces[1][1] + boardColors[1][0] + boardPieces[1][0] + SET_BG_COLOR_LIGHT_GREY + " 2 " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + " 3 " + boardColors[2][7] + boardPieces[2][7] + boardColors[2][6] + boardPieces[2][6] + boardColors[2][5] + boardPieces[2][5] + boardColors[2][4] + boardPieces[2][4] + boardColors[2][3] + boardPieces[2][3] + boardColors[2][2] + boardPieces[2][2] + boardColors[2][1] + boardPieces[2][1] + boardColors[2][0] + boardPieces[2][0] + SET_BG_COLOR_LIGHT_GREY + " 3 " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + " 4 " + boardColors[3][7] + boardPieces[3][7] + boardColors[3][6] + boardPieces[3][6] + boardColors[3][5] + boardPieces[3][5] + boardColors[3][4] + boardPieces[3][4] + boardColors[3][3] + boardPieces[3][3] + boardColors[3][2] + boardPieces[3][2] + boardColors[3][1] + boardPieces[3][1] + boardColors[3][0] + boardPieces[3][0] + SET_BG_COLOR_LIGHT_GREY + " 4 " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + " 5 " + boardColors[4][7] + boardPieces[4][7] + boardColors[4][6] + boardPieces[4][6] + boardColors[4][5] + boardPieces[4][5] + boardColors[4][4] + boardPieces[4][4] + boardColors[4][3] + boardPieces[4][3] + boardColors[4][2] + boardPieces[4][2] + boardColors[4][1] + boardPieces[4][1] + boardColors[4][0] + boardPieces[4][0] + SET_BG_COLOR_LIGHT_GREY + " 5 " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + " 6 " + boardColors[5][7] + boardPieces[5][7] + boardColors[5][6] + boardPieces[5][6] + boardColors[5][5] + boardPieces[5][5] + boardColors[5][4] + boardPieces[5][4] + boardColors[5][3] + boardPieces[5][3] + boardColors[5][2] + boardPieces[5][2] + boardColors[5][1] + boardPieces[5][1] + boardColors[5][0] + boardPieces[5][0] + SET_BG_COLOR_LIGHT_GREY + " 6 " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + " 7 " + boardColors[6][7] + boardPieces[6][7] + boardColors[6][6] + boardPieces[6][6] + boardColors[6][5] + boardPieces[6][5] + boardColors[6][4] + boardPieces[6][4] + boardColors[6][3] + boardPieces[6][3] + boardColors[6][2] + boardPieces[6][2] + boardColors[6][1] + boardPieces[6][1] + boardColors[6][0] + boardPieces[6][0] + SET_BG_COLOR_LIGHT_GREY + " 7 " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + " 8 " + boardColors[7][7] + boardPieces[7][7] + boardColors[7][6] + boardPieces[7][6] + boardColors[7][5] + boardPieces[7][5] + boardColors[7][4] + boardPieces[7][4] + boardColors[7][3] + boardPieces[7][3] + boardColors[7][2] + boardPieces[7][2] + boardColors[7][1] + boardPieces[7][1] + boardColors[7][0] + boardPieces[7][0] + SET_BG_COLOR_LIGHT_GREY + " 8 " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + "    H  G  F  E  D  C  B  A    " + RESET_BG_COLOR + "\n\t" + RESET_TEXT_BOLD_FAINT + RESET_TEXT_COLOR;


                System.out.println(string);

            };

        } else {
            this.board = (boardColors, boardPieces) -> {

                String string = SET_TEXT_BOLD + SET_BG_COLOR_BLACK + "\n\t" + SET_TEXT_COLOR_BLACK +
                        SET_BG_COLOR_LIGHT_GREY + "    A  B  C  D  E  F  G  H    " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + " 8 " + boardColors[7][0] + boardPieces[7][0] + boardColors[7][1] + boardPieces[7][1] + boardColors[7][2] + boardPieces[7][2] + boardColors[7][3] + boardPieces[7][3] + boardColors[7][4] + boardPieces[7][4] + boardColors[7][5] + boardPieces[7][5] + boardColors[7][6] + boardPieces[7][6] + boardColors[7][7] + boardPieces[7][7] + SET_BG_COLOR_LIGHT_GREY + " 8 " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + " 7 " + boardColors[6][0] + boardPieces[6][0] + boardColors[6][1] + boardPieces[6][1] + boardColors[6][2] + boardPieces[6][2] + boardColors[6][3] + boardPieces[6][3] + boardColors[6][4] + boardPieces[6][4] + boardColors[6][5] + boardPieces[6][5] + boardColors[6][6] + boardPieces[6][6] + boardColors[6][7] + boardPieces[6][7] + SET_BG_COLOR_LIGHT_GREY + " 7 " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + " 6 " + boardColors[5][0] + boardPieces[5][0] + boardColors[5][1] + boardPieces[5][1] + boardColors[5][2] + boardPieces[5][2] + boardColors[5][3] + boardPieces[5][3] + boardColors[5][4] + boardPieces[5][4] + boardColors[5][5] + boardPieces[5][5] + boardColors[5][6] + boardPieces[5][6] + boardColors[5][7] + boardPieces[5][7] + SET_BG_COLOR_LIGHT_GREY + " 6 " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + " 5 " + boardColors[4][0] + boardPieces[4][0] + boardColors[4][1] + boardPieces[4][1] + boardColors[4][2] + boardPieces[4][2] + boardColors[4][3] + boardPieces[4][3] + boardColors[4][4] + boardPieces[4][4] + boardColors[4][5] + boardPieces[4][5] + boardColors[4][6] + boardPieces[4][6] + boardColors[4][7] + boardPieces[4][7] + SET_BG_COLOR_LIGHT_GREY + " 5 " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + " 4 " + boardColors[3][0] + boardPieces[3][0] + boardColors[3][1] + boardPieces[3][1] + boardColors[3][2] + boardPieces[3][2] + boardColors[3][3] + boardPieces[3][3] + boardColors[3][4] + boardPieces[3][4] + boardColors[3][5] + boardPieces[3][5] + boardColors[3][6] + boardPieces[3][6] + boardColors[3][7] + boardPieces[3][7] + SET_BG_COLOR_LIGHT_GREY + " 4 " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + " 3 " + boardColors[2][0] + boardPieces[2][0] + boardColors[2][1] + boardPieces[2][1] + boardColors[2][2] + boardPieces[2][2] + boardColors[2][3] + boardPieces[2][3] + boardColors[2][4] + boardPieces[2][4] + boardColors[2][5] + boardPieces[2][5] + boardColors[2][6] + boardPieces[2][6] + boardColors[2][7] + boardPieces[2][7] + SET_BG_COLOR_LIGHT_GREY + " 3 " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + " 2 " + boardColors[1][0] + boardPieces[1][0] + boardColors[1][1] + boardPieces[1][1] + boardColors[1][2] + boardPieces[1][2] + boardColors[1][3] + boardPieces[1][3] + boardColors[1][4] + boardPieces[1][4] + boardColors[1][5] + boardPieces[1][5] + boardColors[1][6] + boardPieces[1][6] + boardColors[1][7] + boardPieces[1][7] + SET_BG_COLOR_LIGHT_GREY + " 2 " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + " 1 " + boardColors[0][0] + boardPieces[0][0] + boardColors[0][1] + boardPieces[0][1] + boardColors[0][2] + boardPieces[0][2] + boardColors[0][3] + boardPieces[0][3] + boardColors[0][4] + boardPieces[0][4] + boardColors[0][5] + boardPieces[0][5] + boardColors[0][6] + boardPieces[0][6] + boardColors[0][7] + boardPieces[0][7] + SET_BG_COLOR_LIGHT_GREY + " 1 " + RESET_BG_COLOR + "\n\t" +
                        SET_BG_COLOR_LIGHT_GREY + "    A  B  C  D  E  F  G  H    " + RESET_BG_COLOR + "\n\t" + RESET_TEXT_BOLD_FAINT + RESET_TEXT_COLOR;

                System.out.println(string);

            };
        }

    }

    @Override
    protected Boolean evaluate(String[] parsedInput) {

//        Check to see if game is in checkmate

        if (playerColor != null && game.isInCheckmate(playerColor)){

        }

        switch (parsedInput[0]) {
            case "help":
                System.out.println( SET_TEXT_COLOR_RED + "\thelp" + SET_TEXT_COLOR_LIGHT_GREY + " - List all valid commands" + SET_TEXT_COLOR_RED + "\n\tr" + SET_TEXT_COLOR_LIGHT_GREY + " - Redraws the chess board" + SET_TEXT_COLOR_RED + "\n\tleave" + SET_TEXT_COLOR_LIGHT_GREY + " - Temporarily leaves the game session" + SET_TEXT_COLOR_RED + "\n\tm <STARTING_COLUMN><STARTING_ROW>  <TARGET_COLUMN><TARGET_ROW>" + SET_TEXT_COLOR_LIGHT_GREY + " - Moves a piece from the starting location to the target location." +  SET_TEXT_COLOR_RED +"\n\th <SELECTED_PIECE'S_COLUMN> <SELECTED_PIECE'S_ROW>" + SET_TEXT_COLOR_LIGHT_GREY + " - Highlights all available moves for the selected piece"+  SET_TEXT_COLOR_RED +"\n\tresign" + SET_TEXT_COLOR_LIGHT_GREY + " - Forfeits the game");
                break;
            case "r":
                this.board.printBoard(colors, pieces);
                break;
            case "leave":
                Leave leave = new Leave(this.authToken, this.gameID);
                String leaveString = this.gson.toJson(leave);
                this.wsf.sendMessage(leaveString);
                this.wsf.disconnect();
                return true;
            case "m":
                if (playerColor != ChessGame.TeamColor.WHITE && playerColor != ChessGame.TeamColor.BLACK){
                    System.out.println(SET_TEXT_COLOR_LIGHT_GREY + "Observers cannot move pieces on the board.");
                    break;
                }

                ChessPosition startPosition = getPosition(parsedInput[1]);
                ChessPosition endPosition = getPosition(parsedInput[2]);

                if (startPosition.getColumn() > 8 || startPosition.getColumn() < 1 || startPosition.getRow() > 8 || startPosition.getRow() < 1 || endPosition.getColumn() > 8 || endPosition.getColumn() < 1 || endPosition.getRow() > 8 || endPosition.getRow() < 1 ) {
                    System.out.println(SET_TEXT_COLOR_LIGHT_GREY + "Invalid move. make sure it is formatted correctly with spaces between.");
                    break;
                }

                MakeMove makeMove = new MakeMove(this.authToken, this.gameID, new ChessMove(startPosition, endPosition, null));
                String makeMoveString = this.gson.toJson(makeMove);
                this.wsf.sendMessage(makeMoveString);
                break;
            case "resign":
                Resign resign = new Resign(this.authToken, this.gameID);
                String resignString = this.gson.toJson(resign);
                this.wsf.sendMessage(resignString);
                this.wsf.disconnect();
                return true;
            case "h":
                ChessPosition highlightPosition = getPosition(parsedInput[1]);

                if (highlightPosition.getColumn() > 7 || highlightPosition.getColumn() < 0 || highlightPosition.getRow() > 7 || highlightPosition.getRow() < 0 ) {
                    System.out.println(SET_TEXT_COLOR_LIGHT_GREY + "Invalid selection. make sure it is formatted correctly with the right location on the board.");
                    break;
                }

                highlightBoard(highlightPosition);
                break;
            default:
                System.out.println(SET_TEXT_COLOR_LIGHT_GREY + "Invalid command. Type help for more information.");
        }
        return false;
    }

    private void highlightBoard(ChessPosition startPosition){

        if (playerColor != null && playerColor == ChessGame.TeamColor.WHITE && game.getBoard().chessBoard[startPosition.getRow()][startPosition.getColumn()] == null){
            System.out.println(SET_TEXT_COLOR_LIGHT_GREY + "no piece at specified location.");
            return;
        }

        ChessPosition actualPosition = new ChessPosition(startPosition.getRow() + 1, startPosition.getColumn() + 1);

        Collection<ChessMove> potentialMoves = game.validMoves(actualPosition);

        if (potentialMoves.isEmpty()){
            System.out.println(SET_TEXT_COLOR_LIGHT_GREY + "no valid moves");
            return;
        }

//        alter the colors array.

        for (ChessMove move : potentialMoves) {

            int rowModifier = move.getEndPosition().getRow() - 1;
            int colModifier = move.getEndPosition().getColumn() - 1;
            if (playerColor != ChessGame.TeamColor.BLACK){
                rowModifier = move.getEndPosition().getRow() - 1;
            } else {
                colModifier = move.getEndPosition().getColumn();
            }


            String color = colors[rowModifier][colModifier];
            if (color.equals(SET_BG_COLOR_WHITE)){
                colors[rowModifier][colModifier] = SET_BG_COLOR_GREEN;
            } else if (color.equals(SET_BG_COLOR_LIGHT_GREY)){
                colors[rowModifier][colModifier] = SET_BG_COLOR_DARK_GREEN;
            }

        }

        this.board.printBoard(colors, pieces);

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
//        how to go from displayed board to game board
        int row = extractNumber(coordinates);;
        int col = 0;


        switch (coordinates.toLowerCase().charAt(0)){
            case 'a':
                col = 1;
                break;
            case 'b':
                col = 2;
                break;
            case 'c':
                col = 3;
                break;
            case 'd':
                col = 4;
                break;
            case 'e':
                col = 5;
                break;
            case 'f':
                col = 6;
                break;
            case 'g':
                col = 7;
                break;
            case 'h':
                col = 8;
                break;
            default:

        }

//        row = 8 - row;

        return new ChessPosition(row, col);
    }

    public static int extractNumber(String input) {
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
                        case ChessPiece.PieceType.PAWN:
                            piece = pieceIsWhite ? WHITE_PAWN : BLACK_PAWN;
                            break;
                        case ChessPiece.PieceType.KNIGHT:
                            piece = pieceIsWhite ? WHITE_KNIGHT : BLACK_KNIGHT;
                            break;
                        case ChessPiece.PieceType.ROOK:
                            piece = pieceIsWhite ? WHITE_ROOK : BLACK_ROOK;
                            break;
                        case ChessPiece.PieceType.BISHOP:
                            piece = pieceIsWhite ? WHITE_BISHOP : BLACK_BISHOP;
                            break;
                        case ChessPiece.PieceType.QUEEN:
                            piece = pieceIsWhite ? WHITE_QUEEN : BLACK_QUEEN;
                            break;
                        case ChessPiece.PieceType.KING:
                            piece = pieceIsWhite ? WHITE_KING : BLACK_KING;
                            break;
                        default:
                            piece = EMPTY;
                            break;
                    }
                }

                this.pieces[i][j] = piece;
            }
        }

        System.out.println(ERASE_SCREEN);
        this.board.printBoard(colors, pieces);
    }


    @Override
    public void printMessage(String message) {
        System.out.println(ERASE_SCREEN);
        this.board.printBoard(colors, pieces);
        System.out.println(SET_TEXT_COLOR_LIGHT_GREY + message);
    }

    @FunctionalInterface
    interface BoardGenerator {
        public void printBoard(String[][] boardColors, String[][] boardPieces);
    }


}

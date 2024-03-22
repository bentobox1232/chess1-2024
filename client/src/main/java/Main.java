import Server.ServerFacade;
import chess.ChessGame;
import chess.ChessPiece;
import model.GameData;
import result.*;
import server.Server;
import ui.EscapeSequences;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Server server;
    private static boolean loggedIn = false;
    private static boolean quit = false;
    private static String authToken =  null;

    private static ServerFacade facade;

    private static List<GameData> games;
    public static void main(String[] args) throws IOException {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade(port);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to 240 chess. Type Help to get started!");

        while(!quit) {
            if (loggedIn) {
                System.out.println("[LOGGED_IN] >>> ");
                postLogin(scanner, facade);
            } else {
                System.out.println("[LOGGED_OUT] >>> ");
                preLogin(scanner, facade);
            }
        }
        scanner.close();
        server.stop();
    }

    private static void preLogin(Scanner scanner, ServerFacade facade) throws IOException {
        String line = scanner.nextLine().toLowerCase();
        String[] arr = line.split(" ");
        switch(arr[0]) {
            case "help":
                help(false);
                break;
            case "register":
                RegisterResult resultR = facade.registerUser(arr[1], arr[2], arr[3]);
                authToken = resultR.getAuthToken();
                loggedIn = true;
                break;
            case "login":
                LoginResult resultL = facade.login(arr[1], arr[2]);
                authToken = resultL.getAuthToken();
                loggedIn = true;
                System.out.println("Welcome "+ resultL.getUsername());
                break;

            case "quit":
                System.out.println("Thank you for playing! See you next time!");
                quit = true;
                return;
            default:
                System.out.println("Please type help to get help, or enter a valid command.");
        }
    }

    private static void postLogin(Scanner scanner, ServerFacade facade) throws IOException {
        String line = scanner.nextLine().toLowerCase();
        String[] arr = line.split(" ");

        ListResult listResult = facade.listGames(authToken);
        games = listResult.getGames();

            switch (arr[0]) {
                case "help":
                    help(true);
                    break;
                case "create":
                    CreateResult resultC = facade.createGame(arr[1], authToken);
                    System.out.println("Game Created ID: "+ resultC.getGameID());
                    break;
                case "list":
                    listGames(authToken);
                    break;
                case "join":
                    joinGame(authToken);
                    break;
                case "observe":
                    joinObserver(authToken);
                    break;
                case "logout":
                    facade.logout(authToken);
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Please tye help to get help, or enter a valid command.");
            }
    }


    public static void listGames(String authToken) {
        //            ListResult listResult = facade.listGames(authToken);
//            games = listResult.getGames();
        if (games != null && !games.isEmpty()) {
            System.out.println("Available games:");
            for (int i = 0; i < games.size(); i++) {
                GameData game = games.get(i);
                System.out.println((i + 1) + ". " + game.getGameName() + " - White: " + game.getWhiteUsername() + ", Black: " + game.getBlackUsername());
            }
        } else {
            System.out.println("No games available.");
        }
    }

    public static void joinObserver(String authToken) {
        if (games == null || games.isEmpty()) {
            System.out.println("No games available to observe.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of the game you want to observe: ");
        int gameNumber = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (gameNumber < 1 || gameNumber > games.size()) {
            System.out.println("Invalid game number.");
            return;
        }

        try {
            GameData selectedGame = games.get(gameNumber - 1);
            int gameID = selectedGame.getGameID(); // Retrieve the actual game ID

            // Call the joinObserver method of ServerFacade
            JoinResult joinResult = facade.joinGame(authToken, gameID, null);
            if (joinResult.isSuccess()) {
                System.out.println("Successfully joined the game as an observer!");
                drawChessboard(selectedGame.getGame().getBoard().getBoard(), true);


                drawChessboard(selectedGame.getGame().getBoard().getBoard(), false);

            } else {
                System.out.println("Failed to join the game as an observer: " + joinResult.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error occurred while joining the game as an observer: " + e.getMessage());
        }
    }


    public static void joinGame(String authToken) {
        if (games == null || games.isEmpty()) {
            System.out.println("No games available to join.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of the game you want to join: ");
        int gameNumber = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (gameNumber < 1 || gameNumber > games.size()) {
            System.out.println("Invalid game number.");
            return;
        }

        try {
            GameData selectedGame = games.get(gameNumber - 1);
            int gameID = selectedGame.getGameID(); // Retrieve the actual game ID
            System.out.print("Enter the color you want to play (WHITE|BLACK|empty): ");
            String color = scanner.nextLine().toUpperCase();

            // Call the joinGame method of ServerFacade
            JoinResult joinResult = facade.joinGame(authToken, gameID, color);
            if (joinResult.isSuccess()) {
                System.out.println("Successfully joined the game!");
                drawChessboard(selectedGame.getGame().getBoard().getBoard(), true);
                drawChessboard(selectedGame.getGame().getBoard().getBoard(), false);
            } else {
                System.out.println("Failed to join the game: " + joinResult.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error occurred while joining the game: " + e.getMessage());
        }
    }

    private static void help(boolean b) {
        if(b) {
            System.out.println("create <NAME> - a game");
            System.out.println("list - games");
            System.out.println("join <ID> [WHITE|BLACK|<empty>] - a game");
            System.out.println("observe <ID> - game");
            System.out.println("logout - when you are done");
            System.out.println("quit - playing chess");
        } else {
            System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - to create an account");
            System.out.println("login <USERNAME> <PASSWORD> - to play chess");
            System.out.println("quit - playing chess");
        }

        System.out.println("help - with possible commands");
    }

    public static void drawChessboard(ChessPiece[][] chessBoard, boolean whiteAtBottom) {
        System.out.println("    " + (whiteAtBottom ? "h     g     f     e     d     c     b     a" : "a     b     c     d     e     f     g     h"));
        int startRow, endRow, rowIncrement;
        if (whiteAtBottom) {
            startRow = 7;
            endRow = 0;
            rowIncrement = -1;
        } else {
            startRow = 0;
            endRow = 7;
            rowIncrement = 1;
        }
        for (int row = startRow; whiteAtBottom ? row >= endRow : row <= endRow; row += rowIncrement) {
            System.out.print((whiteAtBottom ? row + 1 : 8 - row) + "  ");
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = chessBoard[row][col];
                if (piece == null) {
                    System.out.print(EscapeSequences.EMPTY);
                } else {
                    switch (piece.getPieceType()) {
                        case KING:
                            System.out.print(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING);
                            break;
                        case QUEEN:
                            System.out.print(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN);
                            break;
                        case BISHOP:
                            System.out.print(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP);
                            break;
                        case KNIGHT:
                            System.out.print(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT);
                            break;
                        case ROOK:
                            System.out.print(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK);
                            break;
                        case PAWN:
                            System.out.print(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_PAWN : EscapeSequences.BLACK_PAWN);
                            break;
                    }
                }
                if (col < 7) {
                    System.out.print("   ");
                }
            }
            System.out.println("  " + (whiteAtBottom ? row + 1 : 8 - row));
        }
        System.out.println("    " + (whiteAtBottom ? "h     g     f     e     d     c     b     a" : "a     b     c     d     e     f     g     h"));
    }









}


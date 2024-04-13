package Displays;

import Server.ServerFacade;
import model.GameData;
import result.CreateResult;
import result.JoinResult;
import result.ListResult;

import java.io.IOException;
import java.util.List;

import static ui.EscapeSequences.*;

public class PostLogin extends Display {

    private static final String HELP_COMMAND = "help";
    private static final String LOGOUT_COMMAND = "logout";
    private static final String CREATE_COMMAND = "create";
    private static final String LIST_COMMAND = "list";
    private static final String JOIN_COMMAND = "join";

    static String authToken = null;
    private static List<GameData> games;

    @Override
    protected Boolean evaluate(String[] arr) throws Exception {
        switch (arr[0]) {
            case HELP_COMMAND:
                displayHelp();
                break;
            case LOGOUT_COMMAND:
                return handleLogout();
            case CREATE_COMMAND:
                handleCreate(arr);
                break;
            case LIST_COMMAND:
                handleList();
                break;
            case JOIN_COMMAND:
                handleJoin(arr);
                break;
            default:
                System.out.println("Invalid Command\nType help to see all valid commands");
        }
        return false;
    }

    private void displayHelp() {
        System.out.println(SET_TEXT_COLOR_RED + "\thelp" + SET_TEXT_COLOR_LIGHT_GREY + " - List all valid commands" + SET_TEXT_COLOR_RED + "\n\tlogout" + SET_TEXT_COLOR_LIGHT_GREY + " - Logout user." + SET_TEXT_COLOR_RED + "\n\tcreate <Name>" + SET_TEXT_COLOR_LIGHT_GREY + " - Create a game and give it a name" + SET_TEXT_COLOR_RED + "\n\tlist" + SET_TEXT_COLOR_LIGHT_GREY + " - List all available games" +  SET_TEXT_COLOR_RED +"\n\tjoin <ID> <WHITE|BLACK|[empty]>" + SET_TEXT_COLOR_LIGHT_GREY + " - Join an existing game");
    }

    private boolean handleLogout() throws IOException {
        ServerFacade.logout(authToken);
        return true;
    }

    private void handleCreate(String[] arr) throws Exception {
        if (arr.length != 2) {
            System.out.println("Invalid Format\nType 'create' followed by the desired game name");
            return;
        }

        CreateResult resultC = ServerFacade.createGame(arr[1], authToken);
        if (resultC == null) {
            System.out.println("GameID already exists: " + resultC.getMessage());
        } else {
            System.out.println("Game Created ID: " + resultC.getGameID());
        }
    }

    private void handleList() throws IOException {
        ListResult result = ServerFacade.listGames(authToken);
        games = result.getGames();
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

    private void handleJoin(String[] arr) throws Exception {
        if (arr.length < 2 || arr.length > 3) {
            System.out.println("Invalid Format\n Type 'join' followed by which game you want to join, and the color you want to join as. If you just want to observe, omit the color.");
            return;
        }

        GameData selectedGame = games.get(Integer.valueOf(arr[1]) - 1);
        int gameID = selectedGame.getGameID(); // Retrieve the actual game ID

        JoinResult message;
        if (arr.length == 3) {
            message = ServerFacade.joinGame(authToken, gameID, arr[2]);
        } else {
            message = ServerFacade.joinGame(authToken, gameID, null);
        }

        if (message == null) {
            System.out.println("Unable to join game\n Make sure the player color is available by typing 'list'");
            return;
        }

        Gameplay repl;
        if (arr.length == 3) {
            repl = new Gameplay(authToken, gameID, arr[2]);
        } else {
            repl = new Gameplay(authToken, gameID, null);
        }

        repl.start();
        System.out.println(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_LIGHT_GREY + "Quit Game Successfully!");
    }
}

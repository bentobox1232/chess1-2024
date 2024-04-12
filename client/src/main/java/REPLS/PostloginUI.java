package REPLS;

import Server.ServerFacade;
import model.GameData;
import result.CreateResult;
import result.JoinResult;
import result.ListResult;

import java.util.List;

import static ui.EscapeSequences.*;

public class PostloginUI extends REPL {

    public static String authToken = null;

    private static List<GameData> games;

    @Override
    protected Boolean evaluate(String[] arr) throws Exception {

        switch (arr[0]) {
            case "help":
                System.out.println( SET_TEXT_COLOR_RED + "\thelp" + SET_TEXT_COLOR_LIGHT_GREY + " - List all valid commands" + SET_TEXT_COLOR_RED + "\n\tlogout" + SET_TEXT_COLOR_LIGHT_GREY + " - Logout user." + SET_TEXT_COLOR_RED + "\n\tcreate <Name>" + SET_TEXT_COLOR_LIGHT_GREY + " - Create a game and give it a name" + SET_TEXT_COLOR_RED + "\n\tlist" + SET_TEXT_COLOR_LIGHT_GREY + " - List all available games" +  SET_TEXT_COLOR_RED +"\n\tjoin <ID> <WHITE|BLACK|[empty]>" + SET_TEXT_COLOR_LIGHT_GREY + " - Join an existing game");
                break;
            case "logout":
                ServerFacade.logout(authToken);
                return true;
            case "create":
                if (arr.length != 2){
                    System.out.println("Invalid Format\nType 'create' followed by the desired game name");
                    break;
                }
                CreateResult resultC = ServerFacade.createGame(arr[1], authToken);
                if (resultC == null){
                    System.out.println("GameID already exists: "+ resultC.getMessage());
                } else {
                    System.out.println("Game Created ID: "+ resultC.getGameID());
                }
                break;
            case "list":
                ListResult result =  ServerFacade.listGames(authToken);
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
                break;
            case "join":
                if (arr.length < 2 || arr.length > 3){
                    System.out.println("Invalid Format\n Type 'join' followed by which game you want to join, and the color you want to join as. If you just want to observe, omit the color.");
                    break;
                }

                GameData selectedGame = games.get(Integer.valueOf(arr[1]) - 1);
                int gameID = selectedGame.getGameID(); // Retrieve the actual game ID

                JoinResult message = new JoinResult(0,true);
                if (arr.length == 3){
                    message = ServerFacade.joinGame(authToken, gameID, arr[2]);
                } else {
                    message = ServerFacade.joinGame(authToken, gameID, null);

                }
                if (message == null){
                    System.out.println("Unable to join game\n Make sure the player color is available by typing 'list'");
                    break;
                }
                Gameplay repl;
                if (arr.length == 3){
                    repl = new Gameplay(authToken, gameID, arr[2]);
                } else {
                    repl = new Gameplay(authToken, gameID, null);
                }
                repl.start();
                System.out.println(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_LIGHT_GREY + "Quit Game Successfully!");
                break;
            default:
                System.out.println("Invalid Command\nType help to see all valid commands");
        }
        return false;
    }







}

package REPLS;

import Server.ServerFacade;
import result.LoginResult;
import result.RegisterResult;

import static ui.EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class PreloginUI extends REPL {

    @Override
    protected Boolean evaluate(String[] arr) throws Exception {

        switch (arr[0]) {
            case "help":
                System.out.println( SET_TEXT_COLOR_RED + "\thelp" + SET_TEXT_COLOR_LIGHT_GREY + " - List all valid commands" + SET_TEXT_COLOR_RED + "\n\tquit" + SET_TEXT_COLOR_LIGHT_GREY + " - Exit the program" + SET_TEXT_COLOR_RED + "\n\tlogin <USERNAME> <PASSWORD>" + SET_TEXT_COLOR_LIGHT_GREY + " - Login to an existing account" + SET_TEXT_COLOR_RED + "\n\tregister <USERNAME> <PASSWORD> <EMAIL>" + SET_TEXT_COLOR_LIGHT_GREY + " - Register to create an account");
                break;
            case "quit":
                return true;
            case "login":
                LoginResult resultL = ServerFacade.login(arr[1], arr[2]);
                if (resultL == null) {
                    System.out.println("Username or Password is incorrect ");
                } else {
                    System.out.println("Welcome "+ resultL.getUsername());
                    PostloginUI repl = new PostloginUI();
                    PostloginUI.authToken = resultL.getAuthToken();
                    repl.start();
                }
                break;
            case "register":
                RegisterResult resultR = ServerFacade.registerUser(arr[1], arr[2], arr[3]);
                if (resultR == null) {
                    System.out.println("User Already Exist " + arr[1]);
                } else {
                    PostloginUI repl = new PostloginUI();
                    PostloginUI.authToken = resultR.getAuthToken();
                    repl.start();
                }
                break;
            default:
                System.out.println("Invalid Command\nType help to see all valid commands");
        }
        return false;
    }


}

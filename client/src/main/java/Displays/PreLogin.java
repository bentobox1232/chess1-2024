package Displays;

import Server.ServerFacade;
import result.LoginResult;
import result.RegisterResult;

import static ui.EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class PreLogin extends Display {

    @Override
    protected Boolean evaluate(String[] arr) throws Exception {
        switch (arr[0]) {
            case "help":
                displayHelp();
                break;
            case "quit":
                return true;
            case "login":
                handleLogin(arr);
                break;
            case "register":
                handleRegister(arr);
                break;
            default:
                System.out.println("Invalid Command\nType help to see all valid commands");
        }
        return false;
    }

    private void displayHelp() {
        System.out.println(SET_TEXT_COLOR_RED + "\thelp" + SET_TEXT_COLOR_LIGHT_GREY + " - List all valid commands" + SET_TEXT_COLOR_RED + "\n\tquit" + SET_TEXT_COLOR_LIGHT_GREY + " - Exit the program" + SET_TEXT_COLOR_RED + "\n\tlogin <USERNAME> <PASSWORD>" + SET_TEXT_COLOR_LIGHT_GREY + " - Login to an existing account" + SET_TEXT_COLOR_RED + "\n\tregister <USERNAME> <PASSWORD> <EMAIL>" + SET_TEXT_COLOR_LIGHT_GREY + " - Register to create an account");
    }

    private void handleLogin(String[] arr) throws Exception {
        LoginResult resultL = ServerFacade.login(arr[1], arr[2]);
        if (resultL == null) {
            System.out.println("Username or Password is incorrect ");
        } else {
            System.out.println("Welcome " + resultL.getUsername());
            startPostLogin(resultL.getAuthToken());
        }
    }

    private void handleRegister(String[] arr) throws Exception {
        RegisterResult resultR = ServerFacade.registerUser(arr[1], arr[2], arr[3]);
        if (resultR == null) {
            System.out.println("User Already Exist " + arr[1]);
        } else {
            startPostLogin(resultR.getAuthToken());
        }
    }

    private void startPostLogin(String authToken) throws Exception {
        PostLogin postLogin = new PostLogin();
        postLogin.authToken = authToken;
        postLogin.start();
    }
}

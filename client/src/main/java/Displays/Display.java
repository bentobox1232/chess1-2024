package Displays;

import java.util.Scanner;

import static ui.EscapeSequences.ERASE_SCREEN;
import static ui.EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;

public abstract class Display {

    private static final Scanner scanner = new Scanner(System.in);

    public void start() throws Exception {
        while (true) {
            String line = scanner.nextLine().toLowerCase();
            String[] arr = line.split(" ");

            clearScreen();

            System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\n>> ");

            Boolean terminate = evaluate(arr);
            if (terminate) {
                return;
            }
        }
    }

    private void clearScreen() {
        System.out.print(ERASE_SCREEN);
    }

    protected abstract Boolean evaluate(String[] arr) throws Exception;
}

package REPLS;

import java.util.Scanner;

import static ui.EscapeSequences.ERASE_SCREEN;
import static ui.EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;

public abstract class REPL {

    public void start() throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {

            String line = scanner.nextLine().toLowerCase();
            String[] arr = line.split(" ");

            System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\n>> ");

            System.out.println(ERASE_SCREEN);



            Boolean terminate = evaluate(arr);

            if (terminate) { return; }

        }
    }

    protected abstract Boolean evaluate(String[] arr) throws Exception;

}

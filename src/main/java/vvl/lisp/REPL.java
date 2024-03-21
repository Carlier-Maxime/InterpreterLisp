package vvl.lisp;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * A very basic Read Eval Print Loop for your interpreter.
 * <br><br>
 * It is made available to let you play with your work and even to try running
 * real lisp program with it.
 * 
 * @author leberre
 *
 */
public class REPL {
    public static final String START_MSG = "My super own Lisp/Scheme interpreter 2024"+System.lineSeparator()+"Enter a valid Lisp expression followed by Enter. type 'quit' to exit.";

    private REPL() {
        // to prevent instantiation
    }

    public static void readEvalPrintLoop(@NotNull Lisp lisp) {
    	try (var scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {
            System.out.println(START_MSG);
            System.out.print("> ");
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if ("quit".equalsIgnoreCase(line)) {
                    System.out.println("Bye.");
                    return;
                }
                try {
                    System.out.println(lisp.eval(line));
                } catch (LispError le) {
                    System.out.println("Error: " + le.getMessage());
                }
                System.out.print("> ");
            }
        }
    }
    
    public static void main(String[] args) {
        // It would be nice to support expression history as in most common shells.
        // However, it appears that it is not that simple to implement. MR welcome.

        readEvalPrintLoop(Lisp.makeInterpreter());
        
    }
}

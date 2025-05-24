package com.mitrais.flightmanagement.screen;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * Screen class is an abstract class that provides a template for creating different screens in the application.
 * It contains methods for rendering the screen, handling user input, and managing the console output.
 * It also provides utility methods for clearing the screen and holding the screen until the user presses Enter.
 * @param <R> the type of the result that the screen will return
 */
@Slf4j
public abstract class Screen<R> {
    private final Scanner scanner = new Scanner(System.in);

    public R start() {
        return start(true);
    }

    public R startWithoutClearScreen() {
        return start(false);
    }

    private R start(boolean isClearScreen) {
        if (isClearScreen) clearScreen();
        try {
            return renderScreen();
        } catch (Exception e) {
            printError(e.getMessage());
            log.error("Error message: {}", e.getMessage());
            log.error("Error stacktrace: ", e);
            holdScreen();
        }
        return null;
    }

    protected abstract R renderScreen();

    protected String doInput() {
        String s = "";
        s = getScannerNextLine();
        return s;
    }

    protected static void print(String message) {
        System.out.println(message);
    }

    protected static void printError(String message) {
        System.err.println(message);
    }

    protected static void clearScreen() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void holdScreen() {
        getScannerNextLine();
    }

    private String getScannerNextLine() {
        return this.scanner.nextLine();
    }

}

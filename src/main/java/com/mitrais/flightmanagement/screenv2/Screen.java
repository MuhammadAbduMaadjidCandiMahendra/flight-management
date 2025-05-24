package com.mitrais.flightmanagement.screenv2;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Scanner;

@Slf4j
public abstract class Screen {
    protected final ScreenFactory screenFactory;
    private final Scanner scanner;
    private Screen nextScreen;

    protected Screen(ScreenFactory screenFactory, Scanner scanner) {
        this.scanner = scanner;
        this.screenFactory = screenFactory;
    }

    public void start() {
        try {
            renderScreen();
        } catch (Exception e) {
            printError(e.getMessage());
            log.error("Error message: {}", e.getMessage(), e);
        }
    }

    protected abstract void renderScreen();

    public Optional<Screen> nextScreen() {
        return Optional.ofNullable(this.nextScreen);
    }

    public void resetNextScreen() {
        this.nextScreen = null;
    }

    protected String doInput() {
        return this.scanner.nextLine();
    }

    protected void setNextScreen(Screen screen) {
        this.nextScreen = screen;
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
        doInput();
    }
}

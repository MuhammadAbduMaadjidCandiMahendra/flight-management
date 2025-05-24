package com.mitrais.flightmanagement.screenv2;

import java.util.Scanner;

public class MainMenu extends Screen {

    public MainMenu(ScreenFactory screenFactory, Scanner scanner) {
        super(screenFactory, scanner);
    }

    @Override
    protected void renderScreen() {
        clearScreen();
        print("=== SIMPLE FLIGHT BOOKING & RUNNING SYSTEM ===");
        print("");
        print("Login as:");
        print("");
        print("1. Admin");
        print("2. Passenger");
        print("3. Exit");
        final String input = doInput();

        if ("1".equals(input)) {
            setNextScreen(this.screenFactory.getAdminPanelScreen());
        }
    }
}

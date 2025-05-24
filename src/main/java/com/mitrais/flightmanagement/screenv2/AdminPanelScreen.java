package com.mitrais.flightmanagement.screenv2;

import java.util.Scanner;

public class AdminPanelScreen extends Screen {
    private static final String OPTION_REGISTER_AIRCRAFT = "1";
    private static final String OPTION_ADD_DESTINATION = "2";
    private static final String OPTION_CREATE_FLIGHT_ROUTE = "3";
    private static final String OPTION_RUN_BOOKING_SERVICE = "4";
    private static final String OPTION_GO_TO_NEXT_DAY = "5";
    private static final String OPTION_RUN_FLIGHT = "6";
    private static final String OPTION_EXIT = "7";

    public AdminPanelScreen(ScreenFactory screenFactory, Scanner scanner) {
        super(screenFactory, scanner);
    }

    @Override
    protected void renderScreen() {
        boolean isKeepLooping = true;
        do {
            clearScreen();
            print("=== SIMPLE FLIGHT BOOKING & RUNNING SYSTEM ===");
            print("");
            print("1. Register Aircraft");
            print("2. Add Destination");
            print("3. Create Flight Route");
            print("4. Run Booking Service");
            print("5. Go to Next Day");
            print("6. Run Flight");
            print("7. Exit");
            final String input = doInput();

            switch (input) {
                case OPTION_REGISTER_AIRCRAFT -> setNextScreen(this.screenFactory.getRegisterAircraftScreen());
                case OPTION_ADD_DESTINATION -> setNextScreen(this.screenFactory.getAddDestinationScreen());
                case OPTION_CREATE_FLIGHT_ROUTE -> setNextScreen(this.screenFactory.getAddRouteScreen());
                case OPTION_RUN_BOOKING_SERVICE -> setNextScreen(this.screenFactory.getRunServiceScreen());
                case OPTION_GO_TO_NEXT_DAY -> setNextScreen(this.screenFactory.getNextDayScreen());
                case OPTION_RUN_FLIGHT -> setNextScreen(this.screenFactory.getRunFlightScreen());
                case OPTION_EXIT -> {
                    print("Exit admin panel...");
                    holdScreen();
                }
                default -> {
                    printError("Invalid input!");
                    holdScreen();
                    continue;
                }
            }
            isKeepLooping = false;
        } while (isKeepLooping);
    }
}

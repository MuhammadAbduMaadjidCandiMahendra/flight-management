package com.mitrais.flightmanagement.screenv2;

import com.mitrais.flightmanagement.service.SystemOperationalService;

import java.util.Scanner;

public class RunBookingServiceScreen extends Screen {

    private final SystemOperationalService systemOperationalService;

    public RunBookingServiceScreen(ScreenFactory screenFactory, Scanner scanner, SystemOperationalService systemOperationalService) {
        super(screenFactory, scanner);
        this.systemOperationalService = systemOperationalService;
    }

    @Override
    protected void renderScreen() {
        clearScreen();
        print("=== RUN BOOKING SERVICE ===");
        print("");

        systemOperationalService.runningSystemOperational();

        print("Booking service is now running...");
        print("Passenger can now make bookings");
        holdScreen();
    }
}

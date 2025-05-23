package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.service.SystemOperationalService;

public class RunBookingServiceScreen extends Screen<Void> {

    private final SystemOperationalService systemOperationalService;

    public RunBookingServiceScreen(SystemOperationalService systemOperationalService) {
        this.systemOperationalService = systemOperationalService;
    }

    @Override
    protected Void renderScreen() {
        print("=== RUN BOOKING SERVICE ===");
        print("");

        systemOperationalService.runningSystemOperational();

        print("Booking service is now running...");
        print("Passenger can now make bookings");
        holdScreen();
        return null;
    }
}

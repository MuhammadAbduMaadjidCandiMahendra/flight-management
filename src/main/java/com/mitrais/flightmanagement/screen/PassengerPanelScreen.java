package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.entity.Passenger;
import com.mitrais.flightmanagement.service.BookingService;
import com.mitrais.flightmanagement.service.CityService;
import com.mitrais.flightmanagement.service.RouteService;
import com.mitrais.flightmanagement.service.SystemOperationalService;

public class PassengerPanelScreen extends Screen<Void> {
    private static final String OPTION_BOOK_FLIGHT = "1";
    private static final String OPTION_CANCEL_BOOKING = "2";
    private static final String OPTION_EXIT = "3";

    private final Passenger passenger;
    private final CityService cityService;
    private final RouteService routeService;
    private final BookingService bookingService;
    private final SystemOperationalService systemOperationalService;

    public PassengerPanelScreen(Passenger passenger, CityService cityService, RouteService routeService,
                                BookingService bookingService, SystemOperationalService systemOperationalService) {
        this.passenger = passenger;
        this.cityService = cityService;
        this.routeService = routeService;
        this.bookingService = bookingService;
        this.systemOperationalService = systemOperationalService;
    }

    @Override
    protected Void renderScreen() {
        do {
            clearScreen();
            print("=== PASSENGER PANEL (%s) ===".formatted(passenger.getName()));
            print("");
            print("1. Book Flight");
            print("2. Cancel a Booking");
            print("3. Exit");
            final String input = doInput();
            switch (input) {
                case OPTION_BOOK_FLIGHT -> {
                    systemOperationalService.isSystemRunning();
                    final BookingFlightScreen bookingFlightScreen = new BookingFlightScreen(
                            passenger, cityService, routeService, bookingService, systemOperationalService);
                    bookingFlightScreen.start();
                }
                case OPTION_CANCEL_BOOKING -> {
                    systemOperationalService.isSystemRunning();
                    final CancelBookingScreen cancelBookingScreen = new CancelBookingScreen(passenger, bookingService);
                    cancelBookingScreen.start();
                }
                case OPTION_EXIT -> {
                    print("Exiting Passenger Panel...");
                    holdScreen();
                    return null;
                }
                default -> {
                    print("Invalid option. Please try again.");
                    holdScreen();
                }
            }
        } while (true);
    }
}

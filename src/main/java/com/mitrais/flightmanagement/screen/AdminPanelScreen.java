package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.service.AircraftService;
import com.mitrais.flightmanagement.service.CityService;
import com.mitrais.flightmanagement.service.RouteService;
import com.mitrais.flightmanagement.service.SystemOperationalService;

public class AdminPanelScreen extends Screen<Void> {

    private static final String OPTION_REGISTER_AIRCRAFT = "1";
    private static final String OPTION_ADD_DESTINATION = "2";
    private static final String OPTION_CREATE_FLIGHT_ROUTE = "3";
    private static final String OPTION_RUN_BOOKING_SERVICE = "4";
    private static final String OPTION_GO_TO_NEXT_DAY = "5";
    private static final String OPTION_RUN_FLIGHT = "6";
    private static final String OPTION_EXIT = "7";

    private final AircraftService aircraftService;
    private final CityService cityService;
    private final RouteService routeService;
    private final SystemOperationalService systemOperationalService;

    public AdminPanelScreen(AircraftService aircraftService, CityService cityService, RouteService routeService,
                            SystemOperationalService systemOperationalService) {
        this.aircraftService = aircraftService;
        this.cityService = cityService;
        this.routeService = routeService;
        this.systemOperationalService = systemOperationalService;
    }

    @Override
    protected Void renderScreen() {
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
                case OPTION_REGISTER_AIRCRAFT -> {
                    final RegisterAircraftScreen registerAircraftScreen = new RegisterAircraftScreen(aircraftService);
                    registerAircraftScreen.start();
                }
                case OPTION_ADD_DESTINATION -> {
                    final AddDestinationScreen addDestinationScreen = new AddDestinationScreen(cityService);
                    addDestinationScreen.start();
                }
                case OPTION_CREATE_FLIGHT_ROUTE -> {
                    final AddRouteScreen addRouteScreen = new AddRouteScreen(cityService, aircraftService, routeService);
                    addRouteScreen.start();
                }
                case OPTION_RUN_BOOKING_SERVICE -> {
                    final RunBookingServiceScreen runBookingServiceScreen = new RunBookingServiceScreen(systemOperationalService);
                    runBookingServiceScreen.start();
                }
                case OPTION_GO_TO_NEXT_DAY -> {
                    final SimulateNextDayScreen simulateNextDayScreen = new SimulateNextDayScreen(systemOperationalService, routeService);
                    simulateNextDayScreen.start();
                }
                case OPTION_RUN_FLIGHT -> {
                    // todo implement this
                    final RunFlightScreen runFlightScreen = new RunFlightScreen(routeService, systemOperationalService);
                    runFlightScreen.start();
                }
                case OPTION_EXIT -> isKeepLooping = false;
                default -> {
                    printError("Invalid input!");
                    holdScreen();
                }
            }
        } while (isKeepLooping);
        return null;
    }
}

package com.mitrais.flightmanagement.screenv2;

import com.mitrais.flightmanagement.service.*;

import java.util.Scanner;

public class ScreenFactory {
    private final PassengerService passengerService;
    private final CityService cityService;
    private final AircraftService aircraftService;
    private final RouteService routeService;
    private final BookingService bookingService;
    private final SystemOperationalService systemOperationalService;
    private final Scanner scanner = new Scanner(System.in);

    public ScreenFactory(PassengerService passengerService, CityService cityService, AircraftService aircraftService, RouteService routeService, BookingService bookingService, SystemOperationalService systemOperationalService) {
        this.passengerService = passengerService;
        this.cityService = cityService;
        this.aircraftService = aircraftService;
        this.routeService = routeService;
        this.bookingService = bookingService;
        this.systemOperationalService = systemOperationalService;
    }

    public Screen getMainMenuScreen() {
        return new MainMenu(this,scanner);
    }

    public Screen getAdminPanelScreen() {
        return new AdminPanelScreen(this,scanner);
    }

    public Screen getRegisterAircraftScreen() {
        return new RegisterAirCraftScreen(this, scanner, aircraftService);
    }

    public Screen getAddDestinationScreen() {
        return new AddDestinationScreen(this, scanner, cityService);
    }

    public Screen getAddRouteScreen() {
        return new AddRouteScreen(this, scanner, cityService, aircraftService, routeService, systemOperationalService);
    }

    public Screen getRunServiceScreen() {
        return new RunBookingServiceScreen(this, scanner, systemOperationalService);
    }

    public Screen getNextDayScreen() {
        return new SimulateNextDayScreen(this, scanner, systemOperationalService, routeService);
    }

    public Screen getRunFlightScreen() {
        return new RunFlightScreen(this, scanner, routeService, systemOperationalService);
    }
}

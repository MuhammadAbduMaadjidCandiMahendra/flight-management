package com.mitrais.flightmanagement.screenv2;

import com.mitrais.flightmanagement.entity.Aircraft;
import com.mitrais.flightmanagement.entity.City;
import com.mitrais.flightmanagement.entity.Route;
import com.mitrais.flightmanagement.service.AircraftService;
import com.mitrais.flightmanagement.service.CityService;
import com.mitrais.flightmanagement.service.RouteService;
import com.mitrais.flightmanagement.service.SystemOperationalService;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AddRouteScreen extends Screen {

    private final CityService cityService;
    private final AircraftService aircraftService;
    private final RouteService routeService;
    private final SystemOperationalService systemOperationalService;

    public AddRouteScreen(ScreenFactory screenFactory, Scanner scanner, CityService cityService, AircraftService aircraftService, RouteService routeService, SystemOperationalService systemOperationalService) {
        super(screenFactory, scanner);
        this.cityService = cityService;
        this.aircraftService = aircraftService;
        this.routeService = routeService;
        this.systemOperationalService = systemOperationalService;
    }

    @Override
    protected void renderScreen() {
        clearScreen();
        print("=== CREATE FLIGHT ROUTE ===");
        print("");

        final List<City> cities = cityService.getAllCities();
        if (cities.isEmpty()) {
            throw new IllegalStateException("No available destinations.");
        }

        final List<Aircraft> aircraftList = aircraftService.findAllAircraft();
        if (aircraftList.isEmpty()) {
            throw new IllegalStateException("No available aircraft.");
        }

        final String joinCityName = cities.stream()
                .map(City::getName)
                .collect(Collectors.joining(", "));

        print("Available destinations:" + joinCityName);
        print("Enter departure city:");
        final String departureInput = doInput();
        final City departureCity = cityService.findCityByName(departureInput);

        print("Enter destination city:");
        final String destinationInput = doInput();
        final City destinationCity = cityService.findCityByName(destinationInput);

        final String aircraftName = aircraftList.stream().map(Aircraft::getName).collect(Collectors.joining(", "));
        print("Select aircraft: " + aircraftName);
        final String aircraftInput = doInput();
        final Aircraft aircraft = aircraftService.findAircraftByName(aircraftInput);

        print("Enter schedule day:");
        final String scheduleDayInput = doInput();
        final int scheduleDay = parseScheduleDayInput(scheduleDayInput);

        if (scheduleDay <= systemOperationalService.getDayNow().getValue()) {
            throw new IllegalStateException("Schedule day must be greater than the current day.");
        }

        final Route route = routeService.scheduleRoute(departureCity, destinationCity, aircraft, scheduleDay);
        print(
                "Direct flight route created: %s -> %s (%s, Day %s)"
                        .formatted(
                                route.getDepartureCity().getName(), route.getDestinationCity().getName(),
                                route.getAircraft().getName(), route.getScheduleDay().getValue())
        );
        holdScreen();
    }

    private int parseScheduleDayInput(String scheduleDay) {
        try {
            return Integer.parseInt(scheduleDay);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid schedule day: " + scheduleDay + ". Schedule day should be a number!");
        }
    }
}

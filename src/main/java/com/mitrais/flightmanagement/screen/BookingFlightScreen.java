package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.entity.City;
import com.mitrais.flightmanagement.entity.Passenger;
import com.mitrais.flightmanagement.model.BookingDetailResult;
import com.mitrais.flightmanagement.service.BookingService;
import com.mitrais.flightmanagement.service.CityService;
import com.mitrais.flightmanagement.service.RouteService;
import com.mitrais.flightmanagement.service.SystemOperationalService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class BookingFlightScreen extends Screen<Void> {
    private final Passenger passenger;
    private final CityService cityService;
    private final RouteService routeService;
    private final BookingService bookingService;
    private final SystemOperationalService soService;

    public BookingFlightScreen(Passenger passenger,
                               CityService cityService,
                               RouteService routeService,
                               BookingService bookingService,
                               SystemOperationalService soService) {
        this.passenger = passenger;
        this.cityService = cityService;
        this.routeService = routeService;
        this.bookingService = bookingService;
        this.soService = soService;
    }

    @Override
    protected Void renderScreen() {
        print("=== BOOK A FLIGHT ===");
        print("");

        final List<City> cities = cityService.getAllCities();
        if (cities.isEmpty()) {
            print("No available destinations.");
            holdScreen();
            return null;
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

        print("Searching for flights...");
        BookingDetailResult bookingDetailResult = tryDirectFlight(departureCity, destinationCity);
        if (bookingDetailResult == null) {
            bookingDetailResult = tryTransitFlight(departureCity, destinationCity);
        }

        if (bookingDetailResult == null) {
            throw new NoSuchElementException("No direct or transit flight found.");
        }

        final BookingFlightConfirmationScreen bookingFlightConfirmationScreen = new BookingFlightConfirmationScreen(
                bookingDetailResult, passenger, departureCity, destinationCity, bookingService);
        bookingFlightConfirmationScreen.startWithoutClearScreen();
        return null;
    }

    private BookingDetailResult tryDirectFlight(City departureCity, City destinationCity) {
        final BookingDetailScreen directFlightScreen = new BookingDetailDirectFlightScreen(
                passenger, departureCity, destinationCity, routeService, soService);
        return directFlightScreen.startWithoutClearScreen();
    }

    private BookingDetailResult tryTransitFlight(City departureCity, City destinationCity) {
        print("Searching for transit options...");
        final BookingDetailTransitFlightScreen transitFlightScreen = new BookingDetailTransitFlightScreen(
                passenger, departureCity, destinationCity, routeService, soService);
        return transitFlightScreen.startWithoutClearScreen();
    }
}

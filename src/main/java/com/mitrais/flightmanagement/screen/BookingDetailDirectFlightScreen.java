package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.entity.City;
import com.mitrais.flightmanagement.entity.Passenger;
import com.mitrais.flightmanagement.entity.Route;
import com.mitrais.flightmanagement.enums.FlightType;
import com.mitrais.flightmanagement.model.BookingDetailResult;
import com.mitrais.flightmanagement.model.PassengerSeatRoute;
import com.mitrais.flightmanagement.model.SystemDay;
import com.mitrais.flightmanagement.service.RouteService;
import com.mitrais.flightmanagement.service.SystemOperationalService;

import java.util.List;
import java.util.NoSuchElementException;

public class BookingDetailDirectFlightScreen extends BookingDetailScreen {
    private final Passenger passenger;
    private final City departureCity;
    private final City destinationCity;
    private final RouteService routeService;
    private final SystemOperationalService soService;

    public BookingDetailDirectFlightScreen(Passenger passenger, City departureCity, City destinationCity, RouteService routeService, SystemOperationalService soService) {
        this.passenger = passenger;
        this.departureCity = departureCity;
        this.destinationCity = destinationCity;
        this.routeService = routeService;
        this.soService = soService;
    }

    @Override
    protected BookingDetailResult renderScreen() {
        final SystemDay dayNow = soService.getDayNow();
        final List<Route> directRouteList = routeService.searchingDirectRouteFlight(departureCity, destinationCity, dayNow);
        for (final Route route : directRouteList) {
            final String flightLabel = "%s -> %s (Day %s)".formatted(
                    route.getDepartureCity().getName(),
                    route.getDestinationCity().getName(),
                    route.getScheduleDay().getValue());
            print("Found direct flight: " + flightLabel);

            final int seatCapacity = route.getAircraft().getSeatCapacity();
            final int totalBookedSeats = routeService.checkTotalBookedSeat(route);
            final int totalAvailableSeats = seatCapacity - totalBookedSeats;
            if (Math.max(totalAvailableSeats, 0) == 0) {
                printError("No seat available in flight: " + flightLabel);
                continue;
            }

            print("%s seats available".formatted(totalAvailableSeats));

            int passengerSeatNumber = totalAvailableSeats + 1;
            final PassengerSeatRoute seatRoute = new PassengerSeatRoute(passengerSeatNumber, route);
            return new BookingDetailResult(passenger, route.getScheduleDay(), getFlightType(), List.of(seatRoute));
        }
        throw new NoSuchElementException("No direct flight found.");
    }

    @Override
    public FlightType getFlightType() {
        return FlightType.DIRECT;
    }
}

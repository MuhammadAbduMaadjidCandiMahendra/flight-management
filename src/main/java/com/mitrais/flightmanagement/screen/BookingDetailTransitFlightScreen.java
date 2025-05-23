package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.entity.City;
import com.mitrais.flightmanagement.entity.Passenger;
import com.mitrais.flightmanagement.entity.Route;
import com.mitrais.flightmanagement.enums.FlightType;
import com.mitrais.flightmanagement.model.BookingDetailResult;
import com.mitrais.flightmanagement.model.PassengerSeatRoute;
import com.mitrais.flightmanagement.service.RouteService;
import com.mitrais.flightmanagement.service.SystemOperationalService;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.NoSuchElementException;

public class BookingDetailTransitFlightScreen extends BookingDetailScreen {

    private final Passenger passenger;
    private final City departureCity;
    private final City destinationCity;
    private final RouteService routeService;
    private final SystemOperationalService soService;

    public BookingDetailTransitFlightScreen(Passenger passenger, City departureCity, City destinationCity, RouteService routeService, SystemOperationalService soService) {

        this.passenger = passenger;
        this.departureCity = departureCity;
        this.destinationCity = destinationCity;
        this.routeService = routeService;
        this.soService = soService;
    }

    @Override
    protected BookingDetailResult renderScreen() {
        soService.getDayNow();
        final List<Pair<Route, Route>> transitRoutes = routeService.searchingTransitRouteFlight(departureCity, destinationCity, soService.getDayNow());
        for (final Pair<Route, Route> pair : transitRoutes) {
            final Route firstRoute = pair.getFirst();
            final Route secondRoute = pair.getSecond();
            String flightLabel = "%s -> %s -> %s (Day %s)".formatted(
                    firstRoute.getDepartureCity().getName(),
                    secondRoute.getDepartureCity().getName(),
                    secondRoute.getDestinationCity().getName(),
                    secondRoute.getScheduleDay().getValue());
            print("Found transit route: %s".formatted(flightLabel));

            final int totalAvailableSeatFirstRoute = calculateTotalAvailableSeats(firstRoute);
            print("%s available on %s -> %s (Day %s)".formatted(
                    totalAvailableSeatFirstRoute,
                    firstRoute.getDepartureCity().getName(),
                    firstRoute.getDestinationCity().getName(),
                    firstRoute.getScheduleDay().getValue())
            );

            final int totalAvailableSeatSecondRoute = calculateTotalAvailableSeats(secondRoute);
            print("%s available on %s -> %s (Day %s)".formatted(
                    totalAvailableSeatSecondRoute,
                    secondRoute.getDepartureCity().getName(),
                    secondRoute.getDestinationCity().getName(),
                    secondRoute.getScheduleDay().getValue())
            );

            if (totalAvailableSeatFirstRoute == 0 || totalAvailableSeatSecondRoute == 0) {
                printError("No available seats for this route.");
                continue;
            }
            int passengerSeatNumberFirstRoute = totalAvailableSeatFirstRoute + 1;
            PassengerSeatRoute firstSeatRoute = new PassengerSeatRoute(passengerSeatNumberFirstRoute, firstRoute);
            int passengerSeatNumberSecondRoute = totalAvailableSeatSecondRoute + 1;
            PassengerSeatRoute secondSeatRoute = new PassengerSeatRoute(passengerSeatNumberSecondRoute, secondRoute);

            return new BookingDetailResult(passenger, firstRoute.getScheduleDay(), getFlightType(), List.of(firstSeatRoute, secondSeatRoute));
        }
        throw new NoSuchElementException("No transit flight found.");
    }

    private int calculateTotalAvailableSeats(Route route) {
        final int routeSeatCapacity = route.getAircraft().getSeatCapacity();
        final int routeBookedSeat = routeService.checkTotalBookedSeat(route);
        return Math.max((routeSeatCapacity - routeBookedSeat), 0);
    }

    @Override
    public FlightType getFlightType() {
        return FlightType.TRANSIT;
    }
}

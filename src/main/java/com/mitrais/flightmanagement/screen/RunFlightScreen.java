package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.entity.Route;
import com.mitrais.flightmanagement.enums.FlightType;
import com.mitrais.flightmanagement.model.RoutePassengerDto;
import com.mitrais.flightmanagement.model.SystemDay;
import com.mitrais.flightmanagement.service.RouteService;
import com.mitrais.flightmanagement.service.SystemOperationalService;

import java.util.ArrayList;
import java.util.List;

public class RunFlightScreen extends Screen<Void> {
    private final RouteService routeService;
    private final SystemOperationalService systemOperationalService;

    public RunFlightScreen(RouteService routeService, SystemOperationalService systemOperationalService) {
        this.routeService = routeService;
        this.systemOperationalService = systemOperationalService;
    }

    @Override
    protected Void renderScreen() {
        final SystemDay dayNow = systemOperationalService.getDayNow();
        print("=== RUN FLIGHT ===");
        print("");
        print("Running flight for day: %s".formatted(dayNow.getValue()));
        final List<Route> routes = routeService.searchingRouteFlightByScheduleDay(dayNow).stream()
                .filter(r -> Route.RouteState.SCHEDULED.equals(r.getRouteState()))
                .toList();

        if (routes.isEmpty()) {
            print("No flight scheduled today");
            holdScreen();
            return null;
        }

        List<RoutePassengerDto> transitPassenger = new ArrayList<>();
        for (final Route route : routes) {
            print("Processing flight: %s -> %s".formatted(route.getDepartureCity().getName(), route.getDestinationCity().getName()));
            print("Aircraft: %s".formatted(route.getAircraft().getName()));

            final List<RoutePassengerDto> allPassenger = routeService.findAllPassengerByRoute(route);
            print("Passenger boarding: %s".formatted(allPassenger.size()));
            allPassenger.forEach(p -> print("- %s (Seat #%s %s) ".formatted(p.passengerName(), p.seatNumber(), p.flightType().name())));

            routeService.updateState(route, Route.RouteState.DEPARTED);
            print("Flight status: %s".formatted(route.getRouteState().name()));

            routeService.updateState(route, Route.RouteState.ARRIVED);
            print("Flight status: %s".formatted(route.getRouteState().name()));
            print("All passenger already arrived at %s".formatted(route.getDestinationCity().getName()));
            print("");

            transitPassenger = allPassenger.stream().filter(p -> FlightType.TRANSIT.equals(p.flightType())).toList();
        }

        if (!transitPassenger.isEmpty()) {
            print("Transit passengers successfully reached their final destination");
        }
        print("All flights for day %s have been completed successfully");
        holdScreen();
        return null;
    }
}

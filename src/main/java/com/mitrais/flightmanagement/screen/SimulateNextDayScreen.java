package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.entity.Route;
import com.mitrais.flightmanagement.entity.SystemOperational;
import com.mitrais.flightmanagement.model.SystemDay;
import com.mitrais.flightmanagement.service.RouteService;
import com.mitrais.flightmanagement.service.SystemOperationalService;

import java.util.List;

public class SimulateNextDayScreen extends Screen<Void> {

    private final SystemOperationalService systemOperationalService;
    private final RouteService routeService;

    public SimulateNextDayScreen(SystemOperationalService systemOperationalService, RouteService routeService) {
        this.systemOperationalService = systemOperationalService;
        this.routeService = routeService;
    }

    @Override
    protected Void renderScreen() {
        final SystemDay dayNow = systemOperationalService.getDayNow();
        final SystemDay dayTomorrow = dayNow.plus(1);
        final List<Route> routes = routeService.searchingRouteFlightByScheduleDay(dayNow, dayTomorrow)
                .stream()
                .filter(r -> r.getRouteState().equals(Route.RouteState.SCHEDULED))
                .toList();
        print("=== NEXT DAY ===");
        print("");
        print("Current day: %s".formatted(dayNow.getValue()));
        for (final Route route : routes) {
            if (route.getScheduleDay().equals(dayNow)) {
                print("Flight %s -> %s %s for today".formatted(
                        route.getDepartureCity().getName(),
                        route.getDestinationCity().getName(),
                        route.getRouteState().name().toLowerCase()));
            } else {
                print("Flight %s -> %s %s for tomorrow".formatted(
                        route.getDepartureCity().getName(),
                        route.getDestinationCity().getName(),
                        route.getRouteState().name().toLowerCase()));
            }
        }
        print("Confirm go to next day? (y/n)");
        final String input = doInput();
        switch (input) {
        case "y":
            print("Advancing to the next day...");
            final SystemOperational currentState = systemOperationalService.toNextDay();
            print("Success advancing to the next day. Current day is %s".formatted(currentState.getOperationalDay().getValue()));
            holdScreen();
            return null;
        case "n":
        default:
            print("Exit next day menu...");
            holdScreen();
            return null;
        }
    }
}

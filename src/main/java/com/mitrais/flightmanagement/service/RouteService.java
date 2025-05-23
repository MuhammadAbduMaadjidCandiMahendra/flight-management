package com.mitrais.flightmanagement.service;

import com.mitrais.flightmanagement.entity.Aircraft;
import com.mitrais.flightmanagement.entity.City;
import com.mitrais.flightmanagement.entity.Route;
import com.mitrais.flightmanagement.model.RoutePassengerDto;
import com.mitrais.flightmanagement.model.SystemDay;
import com.mitrais.flightmanagement.repository.RouteRepository;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class RouteService {

    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public Route scheduleRoute(City departure, City destination, Aircraft aircraft, Integer scheduleDay) {
        return routeRepository.save(Route.builder()
                .departureCity(departure)
                .destinationCity(destination)
                .aircraft(aircraft)
                .scheduleDay(SystemDay.dayOf(scheduleDay))
                .routeState(Route.RouteState.SCHEDULED)
                .build());
    }

    public List<Route> searchingDirectRouteFlight(City departureCity, City destinationCity, SystemDay dayNow) {
        return routeRepository.findAllByDepartureCityAndDestinationCityAndScheduleDayGreaterThan(departureCity, destinationCity, dayNow);
    }

    /**
     * Searching for transit route flight.
     * Returns a list of pairs of transits route.
     * @param departureCity
     * @param destinationCity
     * @param dayNow
     * @return List of pairs of transits route
     */
    public List<Pair<Route, Route>> searchingTransitRouteFlight(City departureCity, City destinationCity, SystemDay dayNow) {
        List<Pair<Route, Route>> transitRoutes = new ArrayList<>();
        final List<Route> allDestinationRoutes = routeRepository.findAllByDestinationCityAndScheduleDayGreaterThan(destinationCity, dayNow);
        final List<Route> allDepartureRoutes = routeRepository.findAllByDepartureCityAndScheduleDayGreaterThan(departureCity, dayNow);
        for (final Route destinationRoute : allDestinationRoutes) {
            for (final Route departureRoute : allDepartureRoutes) {
                // find intersection/connection between city from route A to B and route B to C
                if (destinationRoute.getDepartureCity().equals(departureRoute.getDestinationCity()) &&
                        destinationRoute.getScheduleDay().equals(departureRoute.getScheduleDay())) {
                    transitRoutes.add(Pair.of(departureRoute, destinationRoute));
                    break;
                }
            }
        }
        return transitRoutes;
    }

    public Integer checkTotalBookedSeat(Route route) {
        return routeRepository.countTotalBookedSeat(route.getRouteId());
    }

    public List<Route> searchingRouteFlightByScheduleDay(SystemDay day) {
        return searchingRouteFlightByScheduleDay(day, day);
    }

    public List<Route> searchingRouteFlightByScheduleDay(SystemDay from, SystemDay to) {
        return routeRepository.findAllByScheduleDayBetweenOrderByScheduleDayAscRouteStateAsc(from, to);
    }

    public List<RoutePassengerDto> findAllPassengerByRoute(Route route) {
        return routeRepository.findAllPassengerByRoute(route.getRouteId());
    }

    public void updateState(Route route, Route.RouteState routeState) {
        route.setRouteState(routeState);
        routeRepository.save(route);
    }
}

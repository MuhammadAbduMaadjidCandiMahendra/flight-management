package com.mitrais.flightmanagement.testutil;

import com.mitrais.flightmanagement.entity.Aircraft;
import com.mitrais.flightmanagement.entity.City;
import com.mitrais.flightmanagement.entity.Route;
import com.mitrais.flightmanagement.entity.SystemOperational;
import com.mitrais.flightmanagement.model.SystemDay;
import com.mitrais.flightmanagement.repository.AircraftRepository;
import com.mitrais.flightmanagement.repository.CityRepository;
import com.mitrais.flightmanagement.repository.RouteRepository;
import com.mitrais.flightmanagement.repository.SystemOperationalRepository;

import java.time.LocalDateTime;
import java.util.List;

public class DataPreparation {

    public static void prepareSystemOperational(SystemOperationalRepository repo) {
        repo.save(SystemOperational.builder()
                .state(SystemOperational.SystemOperationalState.RUNNING)
                .operationalDay(SystemDay.dayOf(2))
                .timestamps(LocalDateTime.now())
                .build());
    }

    public static Aircraft prepareAircraft(AircraftRepository repo) {
        final var aircraftA = Aircraft.builder().name("Jet-1").seatCapacity(5).build();
        return repo.save(aircraftA);
    }

    public static List<City> prepareCities(CityRepository repo) {
        final var cityA = City.builder().name("Jakarta").build();
        final var cityB = City.builder().name("Bali").build();
        final var cityC = City.builder().name("Makassar").build();
        final var cityD = City.builder().name("Surabaya").build();
        return repo.saveAll(List.of(cityA, cityB, cityC, cityD));
    }

    public static void prepareRoute(RouteRepository repo, Aircraft aircraft, City departureCity, City destinationCity, Integer scheduleDay) {
        repo.save(Route.builder()
                .aircraft(aircraft)
                .departureCity(departureCity)
                .destinationCity(destinationCity)
                .scheduleDay(SystemDay.dayOf(scheduleDay))
                .routeState(Route.RouteState.SCHEDULED)
                .build());

    }
}

package com.mitrais.flightmanagement.testutil;

import com.mitrais.flightmanagement.entity.*;
import com.mitrais.flightmanagement.model.SystemDay;
import com.mitrais.flightmanagement.repository.*;

import java.time.LocalDateTime;
import java.util.List;

public class DataPreparation {

    public static final City JAKARTA = City.builder().name("Jakarta").build();
    public static final City BALI = City.builder().name("Bali").build();
    public static final City MAKASSAR = City.builder().name("Makassar").build();
    public static final City SURABAYA = City.builder().name("Surabaya").build();
    public static final Aircraft JET_1 = Aircraft.builder().name("Jet-1").seatCapacity(5).build();
    public static final Passenger PASSENGER_A = Passenger.builder()
            .name("PASSENGER_A")
            .build();

    public static SystemOperational prepareSystemOperational(SystemOperationalRepository repo) {
        return repo.save(SystemOperational.builder()
                .state(SystemOperational.SystemOperationalState.RUNNING)
                .operationalDay(SystemDay.dayOf(2))
                .timestamps(LocalDateTime.now())
                .build());
    }

    public static Aircraft prepareAircraft(AircraftRepository repo) {
        return repo.save(JET_1);
    }

    public static List<City> prepareCities(CityRepository repo) {
        return repo.saveAll(List.of(JAKARTA, BALI, MAKASSAR, SURABAYA));
    }

    public static Passenger preparePassenger(PassengerRepository repo) {
        return repo.save(PASSENGER_A);
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

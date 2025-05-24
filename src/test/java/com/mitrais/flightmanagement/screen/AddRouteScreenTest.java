package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.entity.Aircraft;
import com.mitrais.flightmanagement.entity.City;
import com.mitrais.flightmanagement.entity.SystemOperational;
import com.mitrais.flightmanagement.model.SystemDay;
import com.mitrais.flightmanagement.repository.AircraftRepository;
import com.mitrais.flightmanagement.repository.CityRepository;
import com.mitrais.flightmanagement.repository.RouteRepository;
import com.mitrais.flightmanagement.repository.SystemOperationalRepository;
import com.mitrais.flightmanagement.service.AircraftService;
import com.mitrais.flightmanagement.service.CityService;
import com.mitrais.flightmanagement.service.RouteService;
import com.mitrais.flightmanagement.service.SystemOperationalService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
class AddRouteScreenTest {
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private SystemOperationalRepository systemOperationalRepository;

    private CityService cityService;
    private AircraftService aircraftService;
    private RouteService routeService;
    private SystemOperationalService systemOperationalService;

    @BeforeEach
    void setUp() {
        cityService = new CityService(cityRepository);
        aircraftService = new AircraftService(aircraftRepository);
        routeService = new RouteService(routeRepository);
        systemOperationalService = new SystemOperationalService(systemOperationalRepository);
    }

    private void prepareData() {
        prepareCities();
        prepareAircraft();
        prepareSystemOperational();
    }

    private void prepareSystemOperational() {
        systemOperationalRepository.save(SystemOperational.builder()
                .state(SystemOperational.SystemOperationalState.RUNNING)
                .operationalDay(SystemDay.dayOf(2))
                .timestamps(LocalDateTime.now())
                .build());
    }

    private void prepareAircraft() {
        final var aircraftA = Aircraft.builder().name("Jet-1").seatCapacity(5).build();
        aircraftRepository.save(aircraftA);
    }

    private void prepareCities() {
        final var cityA = City.builder().name("Jakarta").build();
        final var cityB = City.builder().name("Bali").build();
        cityRepository.saveAll(List.of(cityA, cityB));
    }

    @Test
    void addDestination() {
        prepareData();
        MockScanner.input("Jakarta", "Bali", "Jet-1", "5", MockScanner.HIT_ANY);

        final var addRouteScreen = new AddRouteScreen(cityService, aircraftService, routeService, systemOperationalService);
        addRouteScreen.start();

        final var actualRoute = routeRepository.findById(1).orElseThrow();
        Assertions.assertThat(actualRoute)
                .extracting(r -> r.getDepartureCity().getName(), r -> r.getDestinationCity().getName(), r -> r.getAircraft().getName(), r -> r.getScheduleDay().getValue())
                .contains("Jakarta", "Bali", "Jet-1", 5);
    }

    @Test
    void noCitiesAvailable() {
        MockScanner.input("Jakarta", "Bali", "Jet-1", "5", MockScanner.HIT_ANY);

        final var screen = new AddRouteScreen(cityService, aircraftService, routeService, systemOperationalService);
        final var actualError = MockScanner.retrieveErrorMessage(screen::start);
        Assertions.assertThat(actualError).contains("No available destinations.");
    }

    @Test
    void noAircraftAvailable() {
        prepareCities();
        MockScanner.input("Jakarta", "Bali", "Jet-1", "5", MockScanner.HIT_ANY);

        final var screen = new AddRouteScreen(cityService, aircraftService, routeService, systemOperationalService);
        final var actualError = MockScanner.retrieveErrorMessage(screen::start);
        Assertions.assertThat(actualError).contains("No available aircraft.");
    }

    @Test
    void scheduleEqualToday() {
        prepareData();
        MockScanner.input("Jakarta", "Bali", "Jet-1", "2", MockScanner.HIT_ANY);

        final var screen = new AddRouteScreen(cityService, aircraftService, routeService, systemOperationalService);
        final var actualError = MockScanner.retrieveErrorMessage(screen::start);
        Assertions.assertThat(actualError).contains("Schedule day must be greater than the current day.");
    }
}

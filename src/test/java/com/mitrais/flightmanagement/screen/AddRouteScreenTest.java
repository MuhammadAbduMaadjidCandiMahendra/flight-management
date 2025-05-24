package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.repository.AircraftRepository;
import com.mitrais.flightmanagement.repository.CityRepository;
import com.mitrais.flightmanagement.repository.RouteRepository;
import com.mitrais.flightmanagement.repository.SystemOperationalRepository;
import com.mitrais.flightmanagement.service.AircraftService;
import com.mitrais.flightmanagement.service.CityService;
import com.mitrais.flightmanagement.service.RouteService;
import com.mitrais.flightmanagement.service.SystemOperationalService;
import com.mitrais.flightmanagement.testutil.DataPreparation;
import com.mitrais.flightmanagement.testutil.MockScanner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
        DataPreparation.prepareCities(cityRepository);
        DataPreparation.prepareAircraft(aircraftRepository);
        DataPreparation.prepareSystemOperational(systemOperationalRepository);
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
        DataPreparation.prepareCities(cityRepository);
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

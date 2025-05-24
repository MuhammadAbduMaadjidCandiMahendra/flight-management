package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.entity.Booking;
import com.mitrais.flightmanagement.repository.*;
import com.mitrais.flightmanagement.service.BookingService;
import com.mitrais.flightmanagement.service.CityService;
import com.mitrais.flightmanagement.service.RouteService;
import com.mitrais.flightmanagement.service.SystemOperationalService;
import com.mitrais.flightmanagement.testutil.DataPreparation;
import com.mitrais.flightmanagement.testutil.MockScanner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest(showSql = false)
class BookingFlightScreenTest {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private SystemOperationalRepository systemOperationalRepository;

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    private CityService cityService;
    private RouteService routeService;
    private BookingService bookingService;
    private SystemOperationalService systemOperationalService;

    @BeforeAll
    static void beforeAll() {
        System.setProperty("logging.level.org.hibernate.SQL", "OFF");
        System.setProperty("logging.level.org.hibernate.type.descriptor.sql.BasicBinder", "OFF");
    }

    @BeforeEach
    void setUp() {
        cityService = new CityService(cityRepository);
        routeService = new RouteService(routeRepository);
        bookingService = new BookingService(bookingRepository, routeRepository);
        systemOperationalService = new SystemOperationalService(systemOperationalRepository);
    }

    private void prepareData() {
        DataPreparation.preparePassenger(passengerRepository);
        DataPreparation.prepareAircraft(aircraftRepository);
        DataPreparation.prepareCities(cityRepository);
        final var currentState = DataPreparation.prepareSystemOperational(systemOperationalRepository);
        DataPreparation.prepareRoute(routeRepository, DataPreparation.JET_1, DataPreparation.JAKARTA, DataPreparation.BALI, currentState.getOperationalDay().plus(1).getValue());
    }

    @Test
    @Disabled
    void addBooking() {
        prepareData();

        // fixme the scanner in each screen will replace this mock scanner input
        //  thus, required to find different screen approach
        MockScanner.input("Jakarta", "Bali", "dfsadf", "y", MockScanner.HIT_ANY);

        final var screen = new BookingFlightScreen(DataPreparation.PASSENGER_A, cityService, routeService, bookingService, systemOperationalService);
        screen.start();

        final var actualBooking = bookingRepository.findById(1).orElseThrow();
        Assertions.assertThat(actualBooking).extracting(
                        b -> b.getPassenger().getName(),
                        b -> b.getDepartureCity().getName(),
                        b -> b.getDestinationCity().getName(),
                        Booking::getBookingCode)
                .contains("Passenger A", "Jakarta", "Bali", "JAK-BAL-001");

    }
}

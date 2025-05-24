package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.repository.CityRepository;
import com.mitrais.flightmanagement.service.CityService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AddDestinationScreenTest {

    @Autowired
    private CityRepository cityRepository;

    private CityService cityService;

    @BeforeEach
    void setUp() {
        cityService = new CityService(cityRepository);
    }

    @Test
    void addNewDestination() {
        MockScanner.input("Jakarta-test", MockScanner.HIT_ANY);
        final var addDestinationScreen = new AddDestinationScreen(cityService);
        addDestinationScreen.start();

        final var city = cityRepository.findById(1).orElseThrow();
        Assertions.assertThat(city.getName()).isEqualTo("Jakarta-test");
    }

    @Test
    void validateEmptyName() {
        MockScanner.input("", MockScanner.HIT_ANY);
        final var addDestinationScreen = new AddDestinationScreen(cityService);
        final var actualError = MockScanner.retrieveErrorMessage(addDestinationScreen::start);
        System.out.println("the printout: " + actualError);
        Assertions.assertThat(actualError).containsIgnoringCase("Destination name cannot be empty!");
    }
}

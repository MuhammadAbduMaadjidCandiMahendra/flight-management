package com.mitrais.flightmanagement.screenv2;

import com.mitrais.flightmanagement.entity.City;
import com.mitrais.flightmanagement.service.CityService;

import java.util.Scanner;

public class AddDestinationScreen extends Screen {

    private final CityService cityService;

    public AddDestinationScreen(ScreenFactory screenFactory, Scanner scanner, CityService cityService) {
        super(screenFactory, scanner);
        this.cityService = cityService;
    }

    @Override
    protected void renderScreen() {
        clearScreen();
        print("=== ADD DESTINATION ===");
        print("");
        print("Enter destination name:");
        final String destinationName = doInput();

        if (destinationName.isBlank()) {
            throw new IllegalArgumentException("Destination name cannot be empty!");
        }

        final City city = cityService.addCity(destinationName);
        print(city.getName() + " added as a destination!");
        holdScreen();
    }
}

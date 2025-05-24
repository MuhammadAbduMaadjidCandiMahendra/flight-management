package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.entity.City;
import com.mitrais.flightmanagement.service.CityService;

public class AddDestinationScreen extends Screen<Void> {

    private final CityService cityService;

    public AddDestinationScreen(CityService cityService) {
        this.cityService = cityService;
    }

    @Override
    protected Void renderScreen() {
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
        return null;
    }
}

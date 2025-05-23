package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.entity.Aircraft;
import com.mitrais.flightmanagement.service.AircraftService;

public class RegisterAircraftScreen extends Screen<Void> {

    private final AircraftService aircraftService;

    public RegisterAircraftScreen(AircraftService aircraftService) {
        this.aircraftService = aircraftService;
    }

    @Override
    protected Void renderScreen() {
        print("=== REGISTER AIRCRAFT ===");
        print("");
        print("Enter aircraft name:");
        final String aircraftName = doInput();

        print("Enter seat capacity:");
        final String seatCapacityInput = doInput();
        final int seatCapacity = parseSeatCapacityInput(seatCapacityInput);

        final Aircraft aircraft = aircraftService.addAircraft(aircraftName, seatCapacity);
        print(aircraft.getName() + " with " + aircraft.getSeatCapacity() + " seats registered successfully!");
        holdScreen();
        return null;
    }

    private int parseSeatCapacityInput(String seatCapacity) {
        try {
            return Integer.parseInt(seatCapacity);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Invalid seat capacity: " + seatCapacity);
        }
    }
}

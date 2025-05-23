package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.entity.Passenger;
import com.mitrais.flightmanagement.service.PassengerService;

import java.util.Optional;

public class PassengerLoginScreen extends Screen<Passenger> {

    private final PassengerService passengerService;

    public PassengerLoginScreen(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @Override
    protected Passenger renderScreen() {
        print("=== PASSENGER LOGIN ===");
        print("");
        print("Enter passenger name:");
        final String passengerName = doInput();

        Passenger passenger;
        final Optional<Passenger> optionalPassenger = passengerService.findPassenger(passengerName);
        if (optionalPassenger.isPresent()) {
            passenger = optionalPassenger.get();
            print("Welcome back, " + passenger.getName() + "!");
        } else {
            passenger = passengerService.addPassenger(passengerName);
            print("Welcome, " + passenger.getName() + "!");
        }

        holdScreen();
        return passenger;
    }
}

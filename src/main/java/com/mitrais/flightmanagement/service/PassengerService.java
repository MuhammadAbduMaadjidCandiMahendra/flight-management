package com.mitrais.flightmanagement.service;

import com.mitrais.flightmanagement.entity.Passenger;
import com.mitrais.flightmanagement.repository.PassengerRepository;

import java.util.Optional;

public class PassengerService {
    private final PassengerRepository passengerRepository;

    public PassengerService(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public Passenger addPassenger(String name) {
        return passengerRepository.save(Passenger.builder()
                .name(name)
                .build());
    }

    public Optional<Passenger> findPassenger(String passengerName) {
        return passengerRepository.findByNameContainingIgnoreCase(passengerName);
    }
}

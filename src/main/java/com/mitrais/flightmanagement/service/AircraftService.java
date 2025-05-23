package com.mitrais.flightmanagement.service;

import com.mitrais.flightmanagement.entity.Aircraft;
import com.mitrais.flightmanagement.repository.AircraftRepository;

import java.util.List;
import java.util.Optional;

public class AircraftService {
    private final AircraftRepository aircraftRepository;

    public AircraftService(AircraftRepository aircraftRepository) {
        this.aircraftRepository = aircraftRepository;
    }

    public Aircraft addAircraft(String name, Integer seatCapacity) {
        return aircraftRepository.save(Aircraft.builder()
                .name(name)
                .seatCapacity(seatCapacity)
                .build());
    }

    public Aircraft findAircraftByName(String name) {
        final Optional<Aircraft> aircraft = aircraftRepository.findByNameContainingIgnoreCase(name);
        return aircraft.orElseThrow(() -> new IllegalArgumentException("Aircraft not found!"));
    }

    public List<Aircraft> findAllAircraft() {
        return aircraftRepository.findAll();
    }
}

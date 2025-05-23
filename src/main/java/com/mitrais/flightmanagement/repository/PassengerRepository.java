package com.mitrais.flightmanagement.repository;

import com.mitrais.flightmanagement.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
    Optional<Passenger> findByNameContainingIgnoreCase(String name);
}

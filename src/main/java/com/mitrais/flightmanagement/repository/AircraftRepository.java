package com.mitrais.flightmanagement.repository;

import com.mitrais.flightmanagement.entity.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Integer> {
    Optional<Aircraft> findByNameContainingIgnoreCase(String name);
}

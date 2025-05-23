package com.mitrais.flightmanagement.repository;

import com.mitrais.flightmanagement.entity.SystemOperational;
import com.mitrais.flightmanagement.model.SystemDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemOperationalRepository extends JpaRepository<SystemOperational, Integer> {
    @Query("""
            SELECT s FROM SystemOperational s
            WHERE s.operationalDay.value = (SELECT MAX(s2.operationalDay.value) FROM SystemOperational s2)
            ORDER BY s.id DESC
    """)
    List<SystemOperational> findAllByLatestOperationalDay();

    @Query("""
            SELECT s.operationalDay FROM SystemOperational s
            WHERE s.operationalDay.value = (SELECT MAX(s2.operationalDay.value) FROM SystemOperational s2)
            ORDER BY s.id DESC
            LIMIT 1
    """)
    Optional<SystemDay> findLatestOperationalDay();
}

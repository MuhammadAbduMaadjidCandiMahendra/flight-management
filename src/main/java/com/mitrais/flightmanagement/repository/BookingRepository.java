package com.mitrais.flightmanagement.repository;

import com.mitrais.flightmanagement.entity.Booking;
import com.mitrais.flightmanagement.entity.City;
import com.mitrais.flightmanagement.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Integer countAllByDepartureCityAndDestinationCity(City departureCity, City destinationCity);
    List<Booking> findAllByPassengerAndBookingState(Passenger passenger, Booking.BookingState bookingState);
}

package com.mitrais.flightmanagement.repository;

import com.mitrais.flightmanagement.entity.City;
import com.mitrais.flightmanagement.entity.Route;
import com.mitrais.flightmanagement.model.RoutePassengerDto;
import com.mitrais.flightmanagement.model.SystemDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
    List<Route> findAllByDepartureCityAndDestinationCityAndScheduleDayGreaterThan(City departureCity, City destinationCity, SystemDay scheduleDay);
    List<Route> findAllByDepartureCityAndScheduleDayGreaterThan(City departureCity, SystemDay scheduleDay);
    List<Route> findAllByDestinationCityAndScheduleDayGreaterThan(City destinationCity, SystemDay scheduleDay);
    List<Route> findAllByScheduleDayBetweenOrderByScheduleDayAscRouteStateAsc(SystemDay fromDay, SystemDay toDay);
    @Query("""
        SELECT COUNT(bd.seatNumber)
        FROM Route r
        INNER JOIN BookingDetail bd on r.routeId = bd.route.routeId
        INNER JOIN Booking b on bd.booking.bookingId = b.bookingId
        WHERE r.routeId = :routeId
        AND b.bookingState = com.mitrais.flightmanagement.entity.Booking.BookingState.BOOKED
    """)
    Integer countTotalBookedSeat(@Param("routeId") Integer routeId);

    @Query("""
        SELECT new com.mitrais.flightmanagement.model.RoutePassengerDto(r.routeId, p.name, bd.seatNumber, b.bookingId, b.flightType, b.departureCity, b.destinationCity)
        FROM Route r
        INNER JOIN BookingDetail bd on r.routeId = bd.route.routeId
        INNER JOIN Booking b on bd.booking.bookingId = b.bookingId
        INNER JOIN Passenger p on b.passenger.passengerId = p.passengerId
        WHERE r.routeId = :routeId
    """)
    List<RoutePassengerDto> findAllPassengerByRoute(@Param("routeId") Integer routeId);

}

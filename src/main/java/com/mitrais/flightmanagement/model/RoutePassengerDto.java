package com.mitrais.flightmanagement.model;

import com.mitrais.flightmanagement.entity.City;
import com.mitrais.flightmanagement.enums.FlightType;

public record RoutePassengerDto(Integer routeId, String passengerName, Integer seatNumber, Integer bookingId,
                                FlightType flightType, City departure, City destination) {
    public RoutePassengerDto(Integer routeId, String passengerName, Integer seatNumber, Integer bookingId, FlightType flightType, City departure, City destination) {
        this.routeId = routeId;
        this.passengerName = passengerName;
        this.seatNumber = seatNumber;
        this.bookingId = bookingId;
        this.flightType = flightType;
        this.departure = departure;
        this.destination = destination;
    }
}

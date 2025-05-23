package com.mitrais.flightmanagement.model;

import com.mitrais.flightmanagement.entity.Passenger;
import com.mitrais.flightmanagement.enums.FlightType;

import java.util.List;

public record BookingDetailResult(Passenger passenger, SystemDay scheduleDay, FlightType flightType, List<PassengerSeatRoute> passengerSeatRoutes) {
}

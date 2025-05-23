package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.enums.FlightType;
import com.mitrais.flightmanagement.model.BookingDetailResult;

public abstract class BookingDetailScreen extends Screen<BookingDetailResult> {
    public abstract FlightType getFlightType();
}

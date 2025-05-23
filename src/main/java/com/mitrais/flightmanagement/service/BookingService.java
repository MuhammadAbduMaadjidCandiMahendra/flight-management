package com.mitrais.flightmanagement.service;

import com.mitrais.flightmanagement.entity.*;
import com.mitrais.flightmanagement.enums.FlightType;
import com.mitrais.flightmanagement.model.PassengerSeatRoute;
import com.mitrais.flightmanagement.repository.BookingRepository;
import com.mitrais.flightmanagement.repository.RouteRepository;

import java.util.List;

public class BookingService {
    private final BookingRepository bookingRepository;
    private final RouteRepository routeRepository;

    public BookingService(BookingRepository bookingRepository, RouteRepository routeRepository) {
        this.bookingRepository = bookingRepository;
        this.routeRepository = routeRepository;
    }

    public Booking saveBooking(Passenger passenger, FlightType flightType, List<PassengerSeatRoute> seatRoutes,
                               City departureCity, City destinationCity) {
        final Booking booking = Booking.builder()
                .passenger(passenger)
                .departureCity(departureCity)
                .destinationCity(destinationCity)
                .bookingState(Booking.BookingState.BOOKED)
                .bookingCode(generateBookingCode(departureCity, destinationCity))
                .flightType(flightType)
                .build();

        for (final PassengerSeatRoute seatRoute : seatRoutes) {
            final BookingDetail bookingDetail = BookingDetail.builder()
                    .booking(booking)
                    .route(seatRoute.route())
                    .seatNumber(determineSeatNumber(seatRoute.route()))
                    .build();
            booking.addBookingDetail(bookingDetail);
        }
        return bookingRepository.save(booking);
    }

    private Integer determineSeatNumber(Route route) {
        final int totalBookedSeat = routeRepository.countTotalBookedSeat(route.getRouteId());
        return totalBookedSeat + 1;
    }

    private String generateBookingCode(City departureCity, City destinationCity) {
        final int total = bookingRepository.countAllByDepartureCityAndDestinationCity(departureCity, destinationCity);
        return "%s-%s-%03d".formatted(departureCity.getCode(), destinationCity.getCode(), total + 1);
    }

    public List<Booking> findBookingByPassenger(Passenger passenger) {
        return bookingRepository.findAllByPassengerAndBookingState(passenger, Booking.BookingState.BOOKED);
    }

    public void cancelBooking(Booking booking) {
        booking.setBookingState(Booking.BookingState.CANCELLED);
        bookingRepository.save(booking);
    }
}

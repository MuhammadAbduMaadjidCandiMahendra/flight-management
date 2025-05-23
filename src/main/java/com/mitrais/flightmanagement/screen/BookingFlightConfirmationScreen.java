package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.entity.Booking;
import com.mitrais.flightmanagement.entity.BookingDetail;
import com.mitrais.flightmanagement.entity.City;
import com.mitrais.flightmanagement.entity.Passenger;
import com.mitrais.flightmanagement.enums.FlightType;
import com.mitrais.flightmanagement.model.BookingDetailResult;
import com.mitrais.flightmanagement.model.SystemDay;
import com.mitrais.flightmanagement.service.BookingService;

import java.util.stream.Collectors;

public class BookingFlightConfirmationScreen extends Screen<Void> {

    private static final String OPTION_YES = "y";
    private static final String OPTION_NO = "n";

    private final BookingDetailResult bookingDetailResult;
    private final Passenger passenger;
    private final City departureCity;
    private final City destinationCity;
    private final BookingService bookingService;

    public BookingFlightConfirmationScreen(BookingDetailResult bookingDetailResult, Passenger passenger, City departureCity, City destinationCity, BookingService bookingService) {
        this.bookingDetailResult = bookingDetailResult;
        this.passenger = passenger;
        this.departureCity = departureCity;
        this.destinationCity = destinationCity;
        this.bookingService = bookingService;
    }

    @Override
    protected Void renderScreen() {
        do {
            print("Confirm booking? (y/n)");
            final String input = doInput();
            switch (input) {
                case OPTION_YES -> {
                    final Booking booking = bookingService.saveBooking(passenger, bookingDetailResult.flightType(),
                            bookingDetailResult.passengerSeatRoutes(), departureCity, destinationCity);
                    print("Booking confirmed! Booking ID: %s".formatted(booking.getBookingCode()));

                    if (FlightType.DIRECT.equals(booking.getFlightType())) {
                        printDirectFlightDetail(booking);
                    } else {
                        printTransitFlightDetail(booking);
                    }
                    holdScreen();
                    return null;
                }
                case OPTION_NO -> {
                    print("Booking cancelled.");
                    holdScreen();
                    return null;
                }
                default -> {
                    print("Invalid option. Please try again.");
                    print("");
                    holdScreen();
                }
            }
        } while (true);
    }

    private static void printTransitFlightDetail(Booking booking) {
        final String citiesRoute = booking.getAllRoutesCity().stream()
                .map(City::getName)
                .collect(Collectors.joining(" -> "));
        final SystemDay scheduleDay = booking.getFlightScheduleDay();
        print("Details: %s on Day %s".formatted(citiesRoute, scheduleDay.getValue()));

        booking.getBookingDetails().forEach(bd ->
                print("Seat #%s on %s -> %s".formatted(
                        bd.getSeatNumber(),
                        bd.getRoute().getDepartureCity().getName(),
                        bd.getRoute().getDestinationCity().getName())
                ));
    }

    private static void printDirectFlightDetail(Booking booking) {
        final BookingDetail bookingDetail = booking.getBookingDetails().get(0);
        print("Details: %s -> %s on Day %s, seat #%s".formatted(
                booking.getDepartureCity().getName(),
                booking.getDestinationCity().getName(),
                bookingDetail.getRoute().getScheduleDay().getValue(),
                bookingDetail.getSeatNumber())
        );
    }
}

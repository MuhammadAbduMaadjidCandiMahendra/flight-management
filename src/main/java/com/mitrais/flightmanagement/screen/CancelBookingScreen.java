package com.mitrais.flightmanagement.screen;

import com.mitrais.flightmanagement.entity.Booking;
import com.mitrais.flightmanagement.entity.City;
import com.mitrais.flightmanagement.entity.Passenger;
import com.mitrais.flightmanagement.model.SystemDay;
import com.mitrais.flightmanagement.service.BookingService;
import com.mitrais.flightmanagement.util.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CancelBookingScreen extends Screen <Void>{
    private static final String OPTION_YES = "y";
    private static final String OPTION_NO = "n";

    private final Passenger passenger;
    private final BookingService bookingService;

    public CancelBookingScreen(Passenger passenger, BookingService bookingService) {
        this.passenger = passenger;
        this.bookingService = bookingService;
    }

    @Override
    protected Void renderScreen() {
        print("=== CANCEL BOOKING ===");
        print("");
        final List<Booking> bookings = bookingService.findBookingByPassenger(passenger);
        List<Triple<String, String, Booking>> inputOptions = new ArrayList<>();
        int indexNumber = 1;
        for (final Booking booking : bookings) {
            final String citiesRoute = booking.getAllRoutesCity()
                    .stream()
                    .map(City::getName)
                    .collect(Collectors.joining(" - > "));
            final SystemDay scheduleDay = booking.getFlightScheduleDay();
            String routeLabel = "%s on Day %s".formatted(citiesRoute, scheduleDay.getValue());
            print("%s. %s: %s".formatted(indexNumber, booking.getBookingCode(), routeLabel));
            inputOptions.add(Triple.of(booking.getBookingCode(), routeLabel, booking));
            indexNumber++;
        }
        print("Select booking to cancel:");
        final String selectInput = doInput();
        final var inputOption = inputOptions.stream()
                .filter(option -> option.first().equalsIgnoreCase(selectInput))
                .findFirst();

        if (inputOption.isEmpty()) {
            print("Input is not match with booking code");
            holdScreen();
            return null;
        }

        final var selectedBooking = inputOption.get();
        print("Booking Detail:");
        print(selectedBooking.second());
        final Booking booking = selectedBooking.third();
        booking.getBookingDetails().forEach(bd ->
                print("Seat #%s on %s -> %s".formatted(
                        bd.getSeatNumber(),
                        bd.getRoute().getDepartureCity().getName(),
                        bd.getRoute().getDestinationCity().getName())
                ));

        print("Confirm Cancellation? (y/n)");
        final String confirmInput = doInput();
        switch (confirmInput) {
            case OPTION_YES:
                bookingService.cancelBooking(booking);
                print("Booking %s has been cancelled.".formatted(booking.getBookingCode()));
                print("All seats have been released and now are available.");
                holdScreen();
                return null;
            case OPTION_NO:
            default:
                print("Exit cancel booking menu.");
                holdScreen();
                return null;
        }
    }
}

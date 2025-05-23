package com.mitrais.flightmanagement.entity;

import com.mitrais.flightmanagement.enums.FlightType;
import com.mitrais.flightmanagement.model.SystemDay;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    public enum BookingState {
        BOOKED, CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId;

    @ManyToOne
    @JoinColumn(name = "passengerId")
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "departure_city_id")
    private City departureCity;

    @ManyToOne
    @JoinColumn(name = "destination_city_id")
    private City destinationCity;

    @Setter
    @Enumerated(EnumType.STRING)
    private BookingState bookingState;

    private String bookingCode;

    @Enumerated(EnumType.STRING)
    private FlightType flightType;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<BookingDetail> bookingDetails = new ArrayList<>();

    public void addBookingDetail(BookingDetail bookingDetail) {
        if (bookingDetails == null) {
            bookingDetails = new ArrayList<>();
        }
        bookingDetails.add(bookingDetail);
    }

    public Set<City> getAllRoutesCity() {
        return this.getBookingDetails().stream()
                .map(BookingDetail::getRoute)
                .flatMap(route -> Stream.of(route.getDepartureCity(), route.getDestinationCity()))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


    public SystemDay getFlightScheduleDay() {
        return this.getBookingDetails().stream()
                .map(BookingDetail::getRoute)
                .map(Route::getScheduleDay)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Route schedule day not found!"));
    }
}

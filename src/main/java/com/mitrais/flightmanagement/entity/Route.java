package com.mitrais.flightmanagement.entity;

import com.mitrais.flightmanagement.model.SystemDay;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Route {
    public enum RouteState {
        SCHEDULED, DEPARTED, ARRIVED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer routeId;

    @ManyToOne
    @JoinColumn(name = "departure_city_id", nullable = false, unique = false)
    private City departureCity;

    @ManyToOne
    @JoinColumn(name = "destination_city_id", nullable = false, unique = false)
    private City destinationCity;

    @ManyToOne
    @JoinColumn(name = "aircraft_id", nullable = false, unique = false)
    private Aircraft aircraft;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "schedule_day"))
    private SystemDay scheduleDay;

    @Setter
    @Enumerated(EnumType.STRING)
    private RouteState routeState;
}

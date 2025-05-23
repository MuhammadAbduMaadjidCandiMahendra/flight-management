package com.mitrais.flightmanagement.entity;

import com.mitrais.flightmanagement.model.SystemDay;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemOperational {
    public enum SystemOperationalState {
        RUNNING, PENDING, STOP
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "operational_day", nullable = false))
    private SystemDay operationalDay;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private SystemOperationalState state;
    private LocalDateTime timestamps;
}

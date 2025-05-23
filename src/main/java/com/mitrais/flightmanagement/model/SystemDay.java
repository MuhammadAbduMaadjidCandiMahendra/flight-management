package com.mitrais.flightmanagement.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

/**
 * Represent the day for system operational and booking departure.
 */
@Getter
@Embeddable
public class SystemDay {
    private Integer value;

    protected SystemDay() {}

    private SystemDay(Integer value) {
        this.value = value;
    }

    public static SystemDay dayOf(Integer value) {
        return new SystemDay(value);
    }

    public SystemDay plus(Integer day) {
        return new SystemDay(this.value + day);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SystemDay systemDay = (SystemDay) o;
        return Objects.equals(value, systemDay.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

package com.mitrais.flightmanagement.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * Represent a city for departures or destinations
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cityId;
    private String name;

    public String getCode() {
        return name.substring(0, 3).toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final City city = (City) o;
        return Objects.equals(cityId, city.cityId) && Objects.equals(name, city.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityId, name);
    }
}

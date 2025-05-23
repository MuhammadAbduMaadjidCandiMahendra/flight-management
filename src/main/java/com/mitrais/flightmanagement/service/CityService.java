package com.mitrais.flightmanagement.service;

import com.mitrais.flightmanagement.entity.City;
import com.mitrais.flightmanagement.repository.CityRepository;

import java.util.List;
import java.util.Optional;

public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City addCity(String cityName) {
        return cityRepository.save(City.builder()
                .name(cityName)
                .build());
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public City findCityByName(String name) {
        final Optional<City> city = cityRepository.findCityByNameContainingIgnoreCase(name);
        return city.orElseThrow(() -> new IllegalArgumentException("City not found!"));
    }
}

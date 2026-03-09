package com.mourad.backend.application.dto;

import com.mourad.backend.domain.model.City;

import java.util.UUID;

public record CityDto(UUID id, String name) {

    public static CityDto from(City city) {
        return new CityDto(city.getId(), city.getName());
    }
}

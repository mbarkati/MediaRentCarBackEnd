package com.mourad.backend.application.port.in;

import com.mourad.backend.application.dto.CityDto;

import java.util.List;

public interface GetCitiesUseCase {
    List<CityDto> findAll();
}

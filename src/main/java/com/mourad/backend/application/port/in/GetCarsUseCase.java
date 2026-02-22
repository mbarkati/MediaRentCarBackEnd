package com.mourad.backend.application.port.in;

import com.mourad.backend.application.dto.CarDto;
import com.mourad.backend.domain.model.CarStatus;

import java.util.List;

public interface GetCarsUseCase {
    List<CarDto> findAll();
    List<CarDto> findByStatus(CarStatus status);
}

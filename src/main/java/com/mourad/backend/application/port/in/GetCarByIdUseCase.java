package com.mourad.backend.application.port.in;

import com.mourad.backend.application.dto.CarDto;

import java.util.UUID;

public interface GetCarByIdUseCase {
    CarDto execute(UUID id);
}

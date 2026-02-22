package com.mourad.backend.application.port.in;

import com.mourad.backend.application.command.UpdateCarPriceCommand;
import com.mourad.backend.application.dto.CarDto;

import java.util.UUID;

public interface UpdateCarPriceUseCase {
    CarDto execute(UUID id, UpdateCarPriceCommand command);
}

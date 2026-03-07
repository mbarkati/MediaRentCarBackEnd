package com.mourad.backend.application.port.in;

import com.mourad.backend.application.command.UpdateCarCommand;
import com.mourad.backend.application.dto.CarDto;

import java.util.UUID;

public interface UpdateCarUseCase {
    CarDto execute(UUID id, UpdateCarCommand command);
}

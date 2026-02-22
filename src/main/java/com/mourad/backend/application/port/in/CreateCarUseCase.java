package com.mourad.backend.application.port.in;

import com.mourad.backend.application.command.CreateCarCommand;
import com.mourad.backend.application.dto.CarDto;

public interface CreateCarUseCase {
    CarDto execute(CreateCarCommand command);
}

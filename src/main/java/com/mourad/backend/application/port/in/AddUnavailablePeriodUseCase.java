package com.mourad.backend.application.port.in;

import com.mourad.backend.application.command.CreateUnavailablePeriodCommand;
import com.mourad.backend.application.dto.UnavailablePeriodDto;

import java.util.UUID;

public interface AddUnavailablePeriodUseCase {
    UnavailablePeriodDto execute(UUID carId, CreateUnavailablePeriodCommand command);
}

package com.mourad.backend.application.port.in;

import com.mourad.backend.application.command.CreateCityCommand;
import com.mourad.backend.application.dto.CityDto;

public interface CreateCityUseCase {
    CityDto execute(CreateCityCommand command);
}

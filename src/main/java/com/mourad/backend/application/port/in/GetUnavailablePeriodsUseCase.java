package com.mourad.backend.application.port.in;

import com.mourad.backend.application.dto.UnavailablePeriodDto;

import java.util.List;
import java.util.UUID;

public interface GetUnavailablePeriodsUseCase {
    List<UnavailablePeriodDto> findByCarId(UUID carId);
}

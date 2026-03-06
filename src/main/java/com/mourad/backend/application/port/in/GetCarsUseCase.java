package com.mourad.backend.application.port.in;

import com.mourad.backend.application.dto.CarDto;
import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.domain.model.PageResult;

import java.time.LocalDate;
import java.util.List;

public interface GetCarsUseCase {
    List<CarDto> findAll();
    List<CarDto> findByStatus(CarStatus status);
    PageResult<CarDto> findPaged(int page, int size, CarStatus status);
    PageResult<CarDto> findAvailableOnDates(LocalDate startDate, LocalDate endDate, int page, int size);
}

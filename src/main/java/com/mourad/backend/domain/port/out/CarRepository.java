package com.mourad.backend.domain.port.out;

import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.domain.model.PageResult;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository {

    Car save(Car car);

    Optional<Car> findById(UUID id);

    List<Car> findAll();

    List<Car> findByStatus(CarStatus status);

    PageResult<Car> findAllPaged(int page, int size);

    PageResult<Car> findByStatusPaged(CarStatus status, int page, int size);

    PageResult<Car> findAvailableOnDatesPaged(LocalDate startDate, LocalDate endDate, int page, int size);

    void deleteById(UUID id);
}

package com.mourad.backend.domain.port.out;

import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.model.CarStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository {

    Car save(Car car);

    Optional<Car> findById(UUID id);

    List<Car> findAll();

    List<Car> findByStatus(CarStatus status);

    void deleteById(UUID id);
}

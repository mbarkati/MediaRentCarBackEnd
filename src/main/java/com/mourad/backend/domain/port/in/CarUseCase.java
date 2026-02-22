package com.mourad.backend.domain.port.in;

import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.model.CarStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface CarUseCase {

    Car createCar(String brand, String model, int year, BigDecimal dailyPrice, String currency);

    Car getCarById(UUID id);

    List<Car> getAllCars();

    List<Car> getCarsByStatus(CarStatus status);

    /** Full update — all mutable fields. */
    Car updateCar(UUID id, String brand, String model, int year, BigDecimal dailyPrice, String currency);

    /** Focused update — price and currency only. */
    Car updateCarPrice(UUID id, BigDecimal dailyPrice, String currency);

    Car changeCarStatus(UUID id, CarStatus status);

    void deleteCar(UUID id);
}

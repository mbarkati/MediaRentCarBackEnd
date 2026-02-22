package com.mourad.backend.domain.port.in;

import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.domain.model.Money;

import java.util.List;
import java.util.UUID;

public interface CarUseCase {

    Car createCar(String licensePlate, String brand, String model, Money dailyRate);

    Car getCarById(UUID id);

    Car getCarByLicensePlate(String licensePlate);

    List<Car> getAllCars();

    List<Car> getCarsByStatus(CarStatus status);

    Car updateCar(UUID id, String brand, String model, Money dailyRate);

    Car changeCarStatus(UUID id, CarStatus status);

    void deleteCar(UUID id);
}

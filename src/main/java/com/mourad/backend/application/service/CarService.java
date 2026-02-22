package com.mourad.backend.application.service;

import com.mourad.backend.domain.exception.CarNotFoundException;
import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.domain.port.in.CarUseCase;
import com.mourad.backend.domain.port.out.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CarService implements CarUseCase {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public Car createCar(String brand, String model, int year, BigDecimal dailyPrice, String currency) {
        // Domain invariants (year, price, currency) are enforced inside Car.create()
        Car car = Car.create(brand, model, year, dailyPrice, currency);
        return carRepository.save(car);
    }

    @Override
    @Transactional(readOnly = true)
    public Car getCarById(UUID id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Car> getCarsByStatus(CarStatus status) {
        return carRepository.findByStatus(status);
    }

    @Override
    public Car updateCar(UUID id, String brand, String model, int year,
                          BigDecimal dailyPrice, String currency) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        car.update(brand, model, year, dailyPrice, currency);
        return carRepository.save(car);
    }

    @Override
    public Car updateCarPrice(UUID id, BigDecimal dailyPrice, String currency) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        car.updatePrice(dailyPrice, currency);
        return carRepository.save(car);
    }

    @Override
    public Car changeCarStatus(UUID id, CarStatus status) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        car.changeStatus(status);
        return carRepository.save(car);
    }

    @Override
    public void deleteCar(UUID id) {
        carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        carRepository.deleteById(id);
    }
}

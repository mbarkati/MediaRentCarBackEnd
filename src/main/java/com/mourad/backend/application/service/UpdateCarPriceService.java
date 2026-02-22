package com.mourad.backend.application.service;

import com.mourad.backend.application.command.UpdateCarPriceCommand;
import com.mourad.backend.application.dto.CarDto;
import com.mourad.backend.application.port.in.UpdateCarPriceUseCase;
import com.mourad.backend.domain.exception.CarNotFoundException;
import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.port.out.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UpdateCarPriceService implements UpdateCarPriceUseCase {

    private final CarRepository carRepository;

    public UpdateCarPriceService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public CarDto execute(UUID id, UpdateCarPriceCommand command) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        car.updatePrice(command.dailyPrice(), command.currency());
        return CarDto.from(carRepository.save(car));
    }
}

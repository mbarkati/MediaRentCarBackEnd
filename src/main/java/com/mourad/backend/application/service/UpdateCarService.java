package com.mourad.backend.application.service;

import com.mourad.backend.application.command.UpdateCarCommand;
import com.mourad.backend.application.dto.CarDto;
import com.mourad.backend.application.port.in.UpdateCarUseCase;
import com.mourad.backend.domain.exception.CarNotFoundException;
import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.port.out.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UpdateCarService implements UpdateCarUseCase {

    private final CarRepository carRepository;

    public UpdateCarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public CarDto execute(UUID id, UpdateCarCommand command) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        car.update(
                command.brand(),
                command.model(),
                command.year(),
                command.dailyPrice(),
                command.currency(),
                command.seats(),
                command.transmission(),
                command.fuel(),
                command.city(),
                command.imageUrl()
        );
        return CarDto.from(carRepository.save(car));
    }
}

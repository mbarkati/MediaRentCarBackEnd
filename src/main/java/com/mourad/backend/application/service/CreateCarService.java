package com.mourad.backend.application.service;

import com.mourad.backend.application.command.CreateCarCommand;
import com.mourad.backend.application.dto.CarDto;
import com.mourad.backend.application.port.in.CreateCarUseCase;
import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.port.out.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateCarService implements CreateCarUseCase {

    private final CarRepository carRepository;

    public CreateCarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public CarDto execute(CreateCarCommand command) {
        Car car = Car.create(
                command.brand(),
                command.model(),
                command.year(),
                command.dailyPrice(),
                command.currency()
        );
        return CarDto.from(carRepository.save(car));
    }
}

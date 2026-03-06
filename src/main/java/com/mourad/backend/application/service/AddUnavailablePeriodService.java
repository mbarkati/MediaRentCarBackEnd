package com.mourad.backend.application.service;

import com.mourad.backend.application.command.CreateUnavailablePeriodCommand;
import com.mourad.backend.application.dto.UnavailablePeriodDto;
import com.mourad.backend.application.port.in.AddUnavailablePeriodUseCase;
import com.mourad.backend.domain.exception.CarNotFoundException;
import com.mourad.backend.domain.model.UnavailablePeriod;
import com.mourad.backend.domain.port.out.CarRepository;
import com.mourad.backend.domain.port.out.UnavailablePeriodRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class AddUnavailablePeriodService implements AddUnavailablePeriodUseCase {

    private final CarRepository carRepository;
    private final UnavailablePeriodRepository periodRepository;

    public AddUnavailablePeriodService(CarRepository carRepository,
                                       UnavailablePeriodRepository periodRepository) {
        this.carRepository = carRepository;
        this.periodRepository = periodRepository;
    }

    @Override
    public UnavailablePeriodDto execute(UUID carId, CreateUnavailablePeriodCommand command) {
        carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));

        UnavailablePeriod period = UnavailablePeriod.create(
                carId,
                command.startDate(),
                command.endDate(),
                command.reason()
        );
        return UnavailablePeriodDto.from(periodRepository.save(period));
    }
}

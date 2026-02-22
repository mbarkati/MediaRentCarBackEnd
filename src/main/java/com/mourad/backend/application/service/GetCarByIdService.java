package com.mourad.backend.application.service;

import com.mourad.backend.application.dto.CarDto;
import com.mourad.backend.application.port.in.GetCarByIdUseCase;
import com.mourad.backend.domain.exception.CarNotFoundException;
import com.mourad.backend.domain.port.out.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class GetCarByIdService implements GetCarByIdUseCase {

    private final CarRepository carRepository;

    public GetCarByIdService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public CarDto execute(UUID id) {
        return carRepository.findById(id)
                .map(CarDto::from)
                .orElseThrow(() -> new CarNotFoundException(id));
    }
}

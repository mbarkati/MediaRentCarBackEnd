package com.mourad.backend.application.service;

import com.mourad.backend.application.port.in.DeleteCarUseCase;
import com.mourad.backend.domain.exception.CarNotFoundException;
import com.mourad.backend.domain.port.out.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class DeleteCarService implements DeleteCarUseCase {

    private final CarRepository carRepository;

    public DeleteCarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public void execute(UUID id) {
        carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        carRepository.deleteById(id);
    }
}

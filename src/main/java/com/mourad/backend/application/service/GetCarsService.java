package com.mourad.backend.application.service;

import com.mourad.backend.application.dto.CarDto;
import com.mourad.backend.application.port.in.GetCarsUseCase;
import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.domain.model.PageResult;
import com.mourad.backend.domain.port.out.CarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class GetCarsService implements GetCarsUseCase {

    private final CarRepository carRepository;

    public GetCarsService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<CarDto> findAll() {
        return carRepository.findAll().stream()
                .map(CarDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDto> findByStatus(CarStatus status) {
        return carRepository.findByStatus(status).stream()
                .map(CarDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<CarDto> findPaged(int page, int size, CarStatus status) {
        PageResult<Car> raw = status != null
                ? carRepository.findByStatusPaged(status, page, size)
                : carRepository.findAllPaged(page, size);
        List<CarDto> dtos = raw.content().stream().map(CarDto::from).collect(Collectors.toList());
        return new PageResult<>(dtos, raw.totalElements(), raw.totalPages(), raw.currentPage(), raw.first(), raw.last());
    }
}

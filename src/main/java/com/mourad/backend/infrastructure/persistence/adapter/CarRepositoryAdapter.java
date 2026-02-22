package com.mourad.backend.infrastructure.persistence.adapter;

import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.domain.port.out.CarRepository;
import com.mourad.backend.infrastructure.persistence.mapper.CarMapper;
import com.mourad.backend.infrastructure.persistence.repository.CarJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class CarRepositoryAdapter implements CarRepository {

    private final CarJpaRepository jpaRepository;

    public CarRepositoryAdapter(CarJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Car save(Car car) {
        return CarMapper.toDomain(jpaRepository.save(CarMapper.toEntity(car)));
    }

    @Override
    public Optional<Car> findById(UUID id) {
        return jpaRepository.findById(id).map(CarMapper::toDomain);
    }

    @Override
    public List<Car> findAll() {
        return jpaRepository.findAll().stream()
                .map(CarMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> findByStatus(CarStatus status) {
        return jpaRepository.findByStatus(status).stream()
                .map(CarMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}

package com.mourad.backend.infrastructure.persistence.adapter;

import com.mourad.backend.domain.model.UnavailablePeriod;
import com.mourad.backend.domain.port.out.UnavailablePeriodRepository;
import com.mourad.backend.infrastructure.persistence.entity.CarJpaEntity;
import com.mourad.backend.infrastructure.persistence.mapper.UnavailablePeriodMapper;
import com.mourad.backend.infrastructure.persistence.repository.CarJpaRepository;
import com.mourad.backend.infrastructure.persistence.repository.UnavailablePeriodJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class UnavailablePeriodRepositoryAdapter implements UnavailablePeriodRepository {

    private final UnavailablePeriodJpaRepository periodJpaRepository;
    private final CarJpaRepository carJpaRepository;

    public UnavailablePeriodRepositoryAdapter(UnavailablePeriodJpaRepository periodJpaRepository,
                                               CarJpaRepository carJpaRepository) {
        this.periodJpaRepository = periodJpaRepository;
        this.carJpaRepository = carJpaRepository;
    }

    @Override
    public UnavailablePeriod save(UnavailablePeriod period) {
        CarJpaEntity car = carJpaRepository.getReferenceById(period.getCarId());
        return UnavailablePeriodMapper.toDomain(
                periodJpaRepository.save(UnavailablePeriodMapper.toEntity(period, car)));
    }

    @Override
    public List<UnavailablePeriod> findByCarId(UUID carId) {
        return periodJpaRepository.findByCarId(carId).stream()
                .map(UnavailablePeriodMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UnavailablePeriod> findById(UUID id) {
        return periodJpaRepository.findById(id).map(UnavailablePeriodMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        periodJpaRepository.deleteById(id);
    }
}

package com.mourad.backend.infrastructure.persistence.repository;

import com.mourad.backend.infrastructure.persistence.entity.UnavailablePeriodJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UnavailablePeriodJpaRepository extends JpaRepository<UnavailablePeriodJpaEntity, UUID> {

    List<UnavailablePeriodJpaEntity> findByCarId(UUID carId);
}

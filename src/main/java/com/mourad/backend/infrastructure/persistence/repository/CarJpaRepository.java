package com.mourad.backend.infrastructure.persistence.repository;

import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.infrastructure.persistence.entity.CarJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CarJpaRepository extends JpaRepository<CarJpaEntity, UUID> {

    List<CarJpaEntity> findByStatus(CarStatus status);
}

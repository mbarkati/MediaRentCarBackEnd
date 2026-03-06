package com.mourad.backend.infrastructure.persistence.repository;

import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.infrastructure.persistence.entity.CarJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CarJpaRepository extends JpaRepository<CarJpaEntity, UUID> {

    List<CarJpaEntity> findByStatus(CarStatus status);

    Page<CarJpaEntity> findByStatus(CarStatus status, Pageable pageable);

    @Query("SELECT c FROM CarJpaEntity c WHERE c.status = com.mourad.backend.domain.model.CarStatus.AVAILABLE " +
           "AND c.id NOT IN (" +
           "  SELECT up.car.id FROM UnavailablePeriodJpaEntity up " +
           "  WHERE up.startDate <= :endDate AND up.endDate >= :startDate" +
           ")")
    Page<CarJpaEntity> findAvailableOnDates(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate,
                                             Pageable pageable);
}

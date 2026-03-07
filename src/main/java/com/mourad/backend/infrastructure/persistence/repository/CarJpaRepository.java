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

    @Query(value =
               "SELECT c FROM CarJpaEntity c " +
               "WHERE c.status = com.mourad.backend.domain.model.CarStatus.AVAILABLE " +
               "AND (:city IS NULL OR LOWER(c.city) = LOWER(:city)) " +
               "AND NOT EXISTS (" +
               "  SELECT 1 FROM UnavailablePeriodJpaEntity up " +
               "  WHERE up.car = c " +
               "  AND up.startDate <= :endDate AND up.endDate >= :startDate" +
               ")",
           countQuery =
               "SELECT COUNT(c) FROM CarJpaEntity c " +
               "WHERE c.status = com.mourad.backend.domain.model.CarStatus.AVAILABLE " +
               "AND (:city IS NULL OR LOWER(c.city) = LOWER(:city)) " +
               "AND NOT EXISTS (" +
               "  SELECT 1 FROM UnavailablePeriodJpaEntity up " +
               "  WHERE up.car = c " +
               "  AND up.startDate <= :endDate AND up.endDate >= :startDate" +
               ")")
    Page<CarJpaEntity> findAvailableOnDates(@Param("city") String city,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate,
                                             Pageable pageable);
}

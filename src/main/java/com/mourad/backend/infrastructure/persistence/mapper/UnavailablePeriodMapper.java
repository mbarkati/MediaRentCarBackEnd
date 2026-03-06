package com.mourad.backend.infrastructure.persistence.mapper;

import com.mourad.backend.domain.model.UnavailablePeriod;
import com.mourad.backend.infrastructure.persistence.entity.CarJpaEntity;
import com.mourad.backend.infrastructure.persistence.entity.UnavailablePeriodJpaEntity;

public class UnavailablePeriodMapper {

    private UnavailablePeriodMapper() {}

    public static UnavailablePeriod toDomain(UnavailablePeriodJpaEntity entity) {
        return UnavailablePeriod.reconstitute(
                entity.getId(),
                entity.getCar().getId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getReason()
        );
    }

    public static UnavailablePeriodJpaEntity toEntity(UnavailablePeriod period, CarJpaEntity car) {
        UnavailablePeriodJpaEntity entity = new UnavailablePeriodJpaEntity();
        entity.setId(period.getId());
        entity.setCar(car);
        entity.setStartDate(period.getStartDate());
        entity.setEndDate(period.getEndDate());
        entity.setReason(period.getReason());
        return entity;
    }
}

package com.mourad.backend.infrastructure.persistence.mapper;

import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.model.Money;
import com.mourad.backend.infrastructure.persistence.entity.CarJpaEntity;

public class CarMapper {

    private CarMapper() {}

    public static Car toDomain(CarJpaEntity entity) {
        return Car.reconstitute(
                entity.getId(),
                entity.getLicensePlate(),
                entity.getBrand(),
                entity.getModel(),
                Money.of(entity.getDailyRateAmount(), entity.getDailyRateCurrency()),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static CarJpaEntity toEntity(Car car) {
        CarJpaEntity entity = new CarJpaEntity();
        entity.setId(car.getId());
        entity.setLicensePlate(car.getLicensePlate());
        entity.setBrand(car.getBrand());
        entity.setModel(car.getModel());
        entity.setDailyRateAmount(car.getDailyRate().amount());
        entity.setDailyRateCurrency(car.getDailyRate().currency());
        entity.setStatus(car.getStatus());
        entity.setCreatedAt(car.getCreatedAt());
        entity.setUpdatedAt(car.getUpdatedAt());
        return entity;
    }
}

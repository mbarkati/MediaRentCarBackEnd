package com.mourad.backend.infrastructure.persistence.mapper;

import com.mourad.backend.domain.model.Car;
import com.mourad.backend.infrastructure.persistence.entity.CarJpaEntity;

public class CarMapper {

    private CarMapper() {}

    public static Car toDomain(CarJpaEntity entity) {
        return Car.reconstitute(
                entity.getId(),
                entity.getBrand(),
                entity.getModel(),
                entity.getYear(),
                entity.getDailyPrice(),
                entity.getCurrency(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static CarJpaEntity toEntity(Car car) {
        CarJpaEntity entity = new CarJpaEntity();
        entity.setId(car.getId());
        entity.setBrand(car.getBrand());
        entity.setModel(car.getModel());
        entity.setYear(car.getYear());
        entity.setDailyPrice(car.getDailyPrice());
        entity.setCurrency(car.getCurrency());
        entity.setStatus(car.getStatus());
        entity.setCreatedAt(car.getCreatedAt());
        entity.setUpdatedAt(car.getUpdatedAt());
        return entity;
    }
}

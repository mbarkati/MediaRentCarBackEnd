package com.mourad.backend.interfaces.dto.response;

import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.model.CarStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CarResponse(
        UUID id,
        String brand,
        String model,
        int year,
        BigDecimal dailyPrice,
        String currency,
        CarStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CarResponse from(Car car) {
        return new CarResponse(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getYear(),
                car.getDailyPrice(),
                car.getCurrency(),
                car.getStatus(),
                car.getCreatedAt(),
                car.getUpdatedAt()
        );
    }
}

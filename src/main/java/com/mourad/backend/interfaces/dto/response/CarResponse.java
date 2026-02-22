package com.mourad.backend.interfaces.dto.response;

import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.model.CarStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CarResponse(
        UUID id,
        String licensePlate,
        String brand,
        String model,
        BigDecimal dailyRateAmount,
        String dailyRateCurrency,
        CarStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CarResponse from(Car car) {
        return new CarResponse(
                car.getId(),
                car.getLicensePlate(),
                car.getBrand(),
                car.getModel(),
                car.getDailyRate().amount(),
                car.getDailyRate().currency(),
                car.getStatus(),
                car.getCreatedAt(),
                car.getUpdatedAt()
        );
    }
}

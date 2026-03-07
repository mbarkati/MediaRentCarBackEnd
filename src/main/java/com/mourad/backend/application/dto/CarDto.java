package com.mourad.backend.application.dto;

import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.domain.model.Fuel;
import com.mourad.backend.domain.model.Transmission;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CarDto(
        UUID id,
        String brand,
        String model,
        int year,
        BigDecimal pricePerDay,
        String currency,
        CarStatus status,
        boolean isActive,
        Integer seats,
        Transmission transmission,
        Fuel fuel,
        String city,
        String imageUrl,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CarDto from(Car car) {
        return new CarDto(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getYear(),
                car.getDailyPrice(),
                car.getCurrency(),
                car.getStatus(),
                car.getStatus() == CarStatus.AVAILABLE,
                car.getSeats(),
                car.getTransmission(),
                car.getFuel(),
                car.getCity(),
                car.getImageUrl(),
                car.getCreatedAt(),
                car.getUpdatedAt()
        );
    }
}

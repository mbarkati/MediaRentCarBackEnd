package com.mourad.backend.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Car {

    private UUID id;
    private String licensePlate;
    private String brand;
    private String model;
    private Money dailyRate;
    private CarStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Car() {}

    /** Factory method for new cars. */
    public static Car create(String licensePlate, String brand, String model, Money dailyRate) {
        Car car = new Car();
        car.id = UUID.randomUUID();
        car.licensePlate = licensePlate;
        car.brand = brand;
        car.model = model;
        car.dailyRate = dailyRate;
        car.status = CarStatus.AVAILABLE;
        car.createdAt = LocalDateTime.now();
        car.updatedAt = LocalDateTime.now();
        return car;
    }

    /** Factory method used by the mapper to reconstitute a Car from persistence. */
    public static Car reconstitute(UUID id, String licensePlate, String brand, String model,
                                   Money dailyRate, CarStatus status,
                                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        Car car = new Car();
        car.id = id;
        car.licensePlate = licensePlate;
        car.brand = brand;
        car.model = model;
        car.dailyRate = dailyRate;
        car.status = status;
        car.createdAt = createdAt;
        car.updatedAt = updatedAt;
        return car;
    }

    // ----- Business methods -----

    public void changeStatus(CarStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void update(String brand, String model, Money dailyRate) {
        this.brand = brand;
        this.model = model;
        this.dailyRate = dailyRate;
        this.updatedAt = LocalDateTime.now();
    }

    // ----- Getters -----

    public UUID getId() { return id; }
    public String getLicensePlate() { return licensePlate; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public Money getDailyRate() { return dailyRate; }
    public CarStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}

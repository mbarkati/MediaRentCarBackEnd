package com.mourad.backend.infrastructure.persistence.entity;

import com.mourad.backend.domain.model.CarStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cars")
public class CarJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(name = "daily_rate_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyRateAmount;

    @Column(name = "daily_rate_currency", nullable = false, length = 3)
    private String dailyRateCurrency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public CarJpaEntity() {}

    // ----- Getters -----

    public UUID getId() { return id; }
    public String getLicensePlate() { return licensePlate; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public BigDecimal getDailyRateAmount() { return dailyRateAmount; }
    public String getDailyRateCurrency() { return dailyRateCurrency; }
    public CarStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ----- Setters -----

    public void setId(UUID id) { this.id = id; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setModel(String model) { this.model = model; }
    public void setDailyRateAmount(BigDecimal dailyRateAmount) { this.dailyRateAmount = dailyRateAmount; }
    public void setDailyRateCurrency(String dailyRateCurrency) { this.dailyRateCurrency = dailyRateCurrency; }
    public void setStatus(CarStatus status) { this.status = status; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

package com.mourad.backend.domain.model;

import com.mourad.backend.domain.exception.InvalidCarStateException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Car {

    // ── Invariants ────────────────────────────────────────────────────────────
    private static final int MIN_YEAR = 1980;

    // ── State ─────────────────────────────────────────────────────────────────
    private UUID id;
    private String brand;
    private String model;
    private int year;
    private BigDecimal dailyPrice;
    private String currency;
    private CarStatus status;
    private Integer seats;
    private Transmission transmission;
    private Fuel fuel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Car() {}

    // ── Factory — new car ─────────────────────────────────────────────────────

    public static Car create(String brand, String model, int year,
                              BigDecimal dailyPrice, String currency,
                              Integer seats, Transmission transmission, Fuel fuel) {
        validateYear(year);
        validateDailyPrice(dailyPrice);
        validateCurrency(currency);

        Car car = new Car();
        car.id = UUID.randomUUID();
        car.brand = brand;
        car.model = model;
        car.year = year;
        car.dailyPrice = dailyPrice;
        car.currency = currency.toUpperCase();
        car.status = CarStatus.AVAILABLE;
        car.seats = seats;
        car.transmission = transmission;
        car.fuel = fuel;
        car.createdAt = LocalDateTime.now();
        car.updatedAt = LocalDateTime.now();
        return car;
    }

    // ── Factory — reconstitute from persistence (no validation — DB is trusted)

    public static Car reconstitute(UUID id, String brand, String model, int year,
                                    BigDecimal dailyPrice, String currency, CarStatus status,
                                    Integer seats, Transmission transmission, Fuel fuel,
                                    LocalDateTime createdAt, LocalDateTime updatedAt) {
        Car car = new Car();
        car.id = id;
        car.brand = brand;
        car.model = model;
        car.year = year;
        car.dailyPrice = dailyPrice;
        car.currency = currency;
        car.status = status;
        car.seats = seats;
        car.transmission = transmission;
        car.fuel = fuel;
        car.createdAt = createdAt;
        car.updatedAt = updatedAt;
        return car;
    }

    // ── Business methods ──────────────────────────────────────────────────────

    /**
     * Full update of all mutable fields.
     * All invariants are re-validated on each update.
     */
    public void update(String brand, String model, int year,
                        BigDecimal dailyPrice, String currency) {
        validateYear(year);
        validateDailyPrice(dailyPrice);
        validateCurrency(currency);
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.dailyPrice = dailyPrice;
        this.currency = currency.toUpperCase();
        this.updatedAt = LocalDateTime.now();
    }

    /** Focused price-only update. */
    public void updatePrice(BigDecimal dailyPrice, String currency) {
        validateDailyPrice(dailyPrice);
        validateCurrency(currency);
        this.dailyPrice = dailyPrice;
        this.currency = currency.toUpperCase();
        this.updatedAt = LocalDateTime.now();
    }

    public void changeStatus(CarStatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    // ── Invariant guards ──────────────────────────────────────────────────────

    private static void validateYear(int year) {
        int maxYear = LocalDate.now().getYear() + 1;
        if (year < MIN_YEAR || year > maxYear) {
            throw new InvalidCarStateException(
                    "Car year must be between " + MIN_YEAR + " and " + maxYear + " (got: " + year + ")");
        }
    }

    private static void validateDailyPrice(BigDecimal dailyPrice) {
        if (dailyPrice == null || dailyPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidCarStateException("Daily price must be greater than 0");
        }
    }

    /**
     * Validates against the ISO 4217 standard using the JDK built-in registry.
     * Accepts both "eur" and "EUR" — stored in uppercase.
     */
    private static void validateCurrency(String currency) {
        if (currency == null || currency.isBlank()) {
            throw new InvalidCarStateException("Currency must not be blank");
        }
        try {
            java.util.Currency.getInstance(currency.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCarStateException("Invalid ISO 4217 currency code: " + currency);
        }
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public UUID getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public BigDecimal getDailyPrice() { return dailyPrice; }
    public String getCurrency() { return currency; }
    public CarStatus getStatus() { return status; }
    public Integer getSeats() { return seats; }
    public Transmission getTransmission() { return transmission; }
    public Fuel getFuel() { return fuel; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}

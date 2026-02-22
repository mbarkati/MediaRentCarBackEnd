package com.mourad.backend.interfaces.rest;

import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.domain.port.in.CarUseCase;
import com.mourad.backend.interfaces.dto.request.CreateCarRequest;
import com.mourad.backend.interfaces.dto.request.UpdateCarPriceRequest;
import com.mourad.backend.interfaces.dto.request.UpdateCarRequest;
import com.mourad.backend.interfaces.dto.response.CarResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/cars")
@Tag(name = "Cars", description = "Admin operations on the car fleet")
public class CarController {

    private final CarUseCase carUseCase;

    public CarController(CarUseCase carUseCase) {
        this.carUseCase = carUseCase;
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Add a car to the fleet")
    public ResponseEntity<CarResponse> createCar(@Valid @RequestBody CreateCarRequest request) {
        Car car = carUseCase.createCar(
                request.brand(),
                request.model(),
                request.year(),
                request.dailyPrice(),
                request.currency()
        );
        return ResponseEntity
                .created(URI.create("/api/admin/cars/" + car.getId()))
                .body(CarResponse.from(car));
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "List all cars, optionally filtered by status")
    public ResponseEntity<List<CarResponse>> getAllCars(
            @RequestParam(required = false) CarStatus status) {
        List<Car> cars = status != null
                ? carUseCase.getCarsByStatus(status)
                : carUseCase.getAllCars();
        return ResponseEntity.ok(
                cars.stream().map(CarResponse::from).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a car by ID")
    public ResponseEntity<CarResponse> getCarById(@PathVariable UUID id) {
        return ResponseEntity.ok(CarResponse.from(carUseCase.getCarById(id)));
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    @PutMapping("/{id}")
    @Operation(summary = "Update all fields of a car")
    public ResponseEntity<CarResponse> updateCar(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCarRequest request) {
        Car car = carUseCase.updateCar(
                id,
                request.brand(),
                request.model(),
                request.year(),
                request.dailyPrice(),
                request.currency()
        );
        return ResponseEntity.ok(CarResponse.from(car));
    }

    @PatchMapping("/{id}/price")
    @Operation(summary = "Update the daily price (and currency) of a car")
    public ResponseEntity<CarResponse> updateCarPrice(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCarPriceRequest request) {
        Car car = carUseCase.updateCarPrice(id, request.dailyPrice(), request.currency());
        return ResponseEntity.ok(CarResponse.from(car));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change the availability status of a car")
    public ResponseEntity<CarResponse> changeCarStatus(
            @PathVariable UUID id,
            @RequestParam CarStatus status) {
        return ResponseEntity.ok(CarResponse.from(carUseCase.changeCarStatus(id, status)));
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a car from the fleet")
    public ResponseEntity<Void> deleteCar(@PathVariable UUID id) {
        carUseCase.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}

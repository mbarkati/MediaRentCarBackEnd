package com.mourad.backend.interfaces.rest;

import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.domain.model.Money;
import com.mourad.backend.domain.port.in.CarUseCase;
import com.mourad.backend.interfaces.dto.request.CreateCarRequest;
import com.mourad.backend.interfaces.dto.request.UpdateCarRequest;
import com.mourad.backend.interfaces.dto.response.CarResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    private final CarUseCase carUseCase;

    public CarController(CarUseCase carUseCase) {
        this.carUseCase = carUseCase;
    }

    @PostMapping
    public ResponseEntity<CarResponse> createCar(@Valid @RequestBody CreateCarRequest request) {
        Car car = carUseCase.createCar(
                request.licensePlate(),
                request.brand(),
                request.model(),
                Money.of(request.dailyRateAmount(), request.dailyRateCurrency())
        );
        return ResponseEntity
                .created(URI.create("/api/v1/cars/" + car.getId()))
                .body(CarResponse.from(car));
    }

    @GetMapping
    public ResponseEntity<List<CarResponse>> getAllCars(
            @RequestParam(required = false) CarStatus status) {
        List<Car> cars = status != null
                ? carUseCase.getCarsByStatus(status)
                : carUseCase.getAllCars();
        return ResponseEntity.ok(
                cars.stream().map(CarResponse::from).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable UUID id) {
        return ResponseEntity.ok(CarResponse.from(carUseCase.getCarById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCar(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCarRequest request) {
        Car car = carUseCase.updateCar(
                id,
                request.brand(),
                request.model(),
                Money.of(request.dailyRateAmount(), request.dailyRateCurrency())
        );
        return ResponseEntity.ok(CarResponse.from(car));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CarResponse> changeCarStatus(
            @PathVariable UUID id,
            @RequestParam CarStatus status) {
        return ResponseEntity.ok(CarResponse.from(carUseCase.changeCarStatus(id, status)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable UUID id) {
        carUseCase.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}

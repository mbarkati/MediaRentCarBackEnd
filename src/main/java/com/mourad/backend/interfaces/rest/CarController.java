package com.mourad.backend.interfaces.rest;

import com.mourad.backend.application.command.CreateCarCommand;
import com.mourad.backend.application.command.UpdateCarPriceCommand;
import com.mourad.backend.application.dto.CarDto;
import com.mourad.backend.application.port.in.CreateCarUseCase;
import com.mourad.backend.application.port.in.DeleteCarUseCase;
import com.mourad.backend.application.port.in.GetCarByIdUseCase;
import com.mourad.backend.application.port.in.GetCarsUseCase;
import com.mourad.backend.application.port.in.UpdateCarPriceUseCase;
import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.interfaces.dto.request.CreateCarRequest;
import com.mourad.backend.interfaces.dto.request.UpdateCarPriceRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/cars")
@Tag(name = "Cars", description = "Admin operations on the car fleet")
public class CarController {

    private final CreateCarUseCase createCarUseCase;
    private final DeleteCarUseCase deleteCarUseCase;
    private final UpdateCarPriceUseCase updateCarPriceUseCase;
    private final GetCarsUseCase getCarsUseCase;
    private final GetCarByIdUseCase getCarByIdUseCase;

    public CarController(CreateCarUseCase createCarUseCase,
                         DeleteCarUseCase deleteCarUseCase,
                         UpdateCarPriceUseCase updateCarPriceUseCase,
                         GetCarsUseCase getCarsUseCase,
                         GetCarByIdUseCase getCarByIdUseCase) {
        this.createCarUseCase = createCarUseCase;
        this.deleteCarUseCase = deleteCarUseCase;
        this.updateCarPriceUseCase = updateCarPriceUseCase;
        this.getCarsUseCase = getCarsUseCase;
        this.getCarByIdUseCase = getCarByIdUseCase;
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Add a car to the fleet")
    public ResponseEntity<CarDto> createCar(@Valid @RequestBody CreateCarRequest request) {
        CarDto dto = createCarUseCase.execute(new CreateCarCommand(
                request.brand(),
                request.model(),
                request.year(),
                request.dailyPrice(),
                request.currency()
        ));
        return ResponseEntity
                .created(URI.create("/api/admin/cars/" + dto.id()))
                .body(dto);
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "List all cars, optionally filtered by status")
    public ResponseEntity<List<CarDto>> getAllCars(
            @RequestParam(required = false) CarStatus status) {
        List<CarDto> cars = status != null
                ? getCarsUseCase.findByStatus(status)
                : getCarsUseCase.findAll();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a car by ID")
    public ResponseEntity<CarDto> getCarById(@PathVariable UUID id) {
        return ResponseEntity.ok(getCarByIdUseCase.execute(id));
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    @PatchMapping("/{id}/price")
    @Operation(summary = "Update the daily price (and currency) of a car")
    public ResponseEntity<CarDto> updateCarPrice(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCarPriceRequest request) {
        CarDto dto = updateCarPriceUseCase.execute(
                id, new UpdateCarPriceCommand(request.dailyPrice(), request.currency()));
        return ResponseEntity.ok(dto);
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a car from the fleet")
    public ResponseEntity<Void> deleteCar(@PathVariable UUID id) {
        deleteCarUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}

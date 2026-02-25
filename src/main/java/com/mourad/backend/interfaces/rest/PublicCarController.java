package com.mourad.backend.interfaces.rest;

import com.mourad.backend.application.dto.CarDto;
import com.mourad.backend.application.port.in.GetCarByIdUseCase;
import com.mourad.backend.application.port.in.GetCarsUseCase;
import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.domain.model.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cars")
@Validated
@Tag(name = "Public Cars", description = "Browse the available car fleet (no auth required)")
public class PublicCarController {

    private final GetCarsUseCase getCarsUseCase;
    private final GetCarByIdUseCase getCarByIdUseCase;

    public PublicCarController(GetCarsUseCase getCarsUseCase, GetCarByIdUseCase getCarByIdUseCase) {
        this.getCarsUseCase = getCarsUseCase;
        this.getCarByIdUseCase = getCarByIdUseCase;
    }

    @GetMapping
    @Operation(summary = "List cars (optionally filtered by status, with pagination)")
    public ResponseEntity<PageResult<CarDto>> getCars(
            @RequestParam(required = false) CarStatus status,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        return ResponseEntity.ok(getCarsUseCase.findPaged(page, size, status));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a car by ID")
    public ResponseEntity<CarDto> getCarById(@PathVariable UUID id) {
        return ResponseEntity.ok(getCarByIdUseCase.execute(id));
    }
}

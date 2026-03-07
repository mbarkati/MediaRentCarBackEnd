package com.mourad.backend.interfaces.rest;

import com.mourad.backend.application.dto.CarDto;
import com.mourad.backend.application.port.in.GetCarByIdUseCase;
import com.mourad.backend.application.port.in.GetCarsUseCase;
import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.domain.model.PageResult;
import com.mourad.backend.interfaces.dto.response.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/cars")
@Validated
@Tag(name = "Public Cars", description = "Browse the car fleet — no authentication required")
public class PublicCarController {

    private final GetCarsUseCase getCarsUseCase;
    private final GetCarByIdUseCase getCarByIdUseCase;

    public PublicCarController(GetCarsUseCase getCarsUseCase, GetCarByIdUseCase getCarByIdUseCase) {
        this.getCarsUseCase = getCarsUseCase;
        this.getCarByIdUseCase = getCarByIdUseCase;
    }

    @GetMapping
    @Operation(
            summary = "List cars",
            description = "Returns a paginated list of cars. " +
                    "When `startDate` and `endDate` are both provided, only returns cars with status AVAILABLE " +
                    "that have no unavailability period overlapping the requested range. " +
                    "Without dates, all cars are returned (optionally filtered by status)."
    )
    @ApiResponse(responseCode = "200", description = "Paginated car list",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(name = "page-0", value = """
                            {
                              "content": [
                                {
                                  "id": "550e8400-e29b-41d4-a716-446655440000",
                                  "brand": "Renault",
                                  "model": "Clio",
                                  "year": 2022,
                                  "dailyPrice": 45.00,
                                  "currency": "EUR",
                                  "status": "AVAILABLE",
                                  "createdAt": "2026-02-25T10:00:00",
                                  "updatedAt": "2026-02-25T10:00:00"
                                }
                              ],
                              "totalElements": 42,
                              "totalPages": 3,
                              "currentPage": 0,
                              "first": true,
                              "last": false
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Invalid pagination parameters",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2026-02-25T20:00:00.000Z",
                              "status": 400,
                              "error": "BAD_REQUEST",
                              "message": "size: must be less than or equal to 100",
                              "path": "/api/cars"
                            }
                            """)))
    public ResponseEntity<PageResult<CarDto>> getCars(
            @Parameter(description = "City filter — only cars available in this city", example = "Casablanca")
            @RequestParam(required = false) String city,

            @Parameter(description = "Availability start date (ISO 8601). Requires 'to'.", example = "2026-03-10")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,

            @Parameter(description = "Availability end date (ISO 8601). Requires 'from'.", example = "2026-03-15")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,

            @Parameter(description = "Filter by status — ignored when from/to are provided", example = "AVAILABLE")
            @RequestParam(required = false) CarStatus status,

            @Parameter(description = "Zero-based page index", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,

            @Parameter(description = "Page size — between 1 and 100", example = "20")
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        if (from != null && to != null) {
            return ResponseEntity.ok(getCarsUseCase.findAvailableOnDates(city, from, to, page, size));
        }
        return ResponseEntity.ok(getCarsUseCase.findPaged(page, size, status));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a car by ID")
    @ApiResponse(responseCode = "200", description = "Car found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CarDto.class),
                    examples = @ExampleObject(value = """
                            {
                              "id": "550e8400-e29b-41d4-a716-446655440000",
                              "brand": "Renault",
                              "model": "Clio",
                              "year": 2022,
                              "dailyPrice": 45.00,
                              "currency": "EUR",
                              "status": "AVAILABLE",
                              "createdAt": "2026-02-25T10:00:00",
                              "updatedAt": "2026-02-25T10:00:00"
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Malformed UUID",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2026-02-25T20:00:00.000Z",
                              "status": 400,
                              "error": "BAD_REQUEST",
                              "message": "Invalid value 'not-a-uuid' for parameter 'id'",
                              "path": "/api/cars/not-a-uuid"
                            }
                            """)))
    @ApiResponse(responseCode = "404", description = "Car not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2026-02-25T20:00:00.000Z",
                              "status": 404,
                              "error": "NOT_FOUND",
                              "message": "Car not found with id: 550e8400-e29b-41d4-a716-446655440000",
                              "path": "/api/cars/550e8400-e29b-41d4-a716-446655440000"
                            }
                            """)))
    public ResponseEntity<CarDto> getCarById(
            @Parameter(description = "Car UUID", example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable UUID id) {
        return ResponseEntity.ok(getCarByIdUseCase.execute(id));
    }
}

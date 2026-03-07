package com.mourad.backend.interfaces.rest;

import com.mourad.backend.application.command.CreateCarCommand;
import com.mourad.backend.application.command.UpdateCarCommand;
import com.mourad.backend.application.command.UpdateCarPriceCommand;
import com.mourad.backend.application.dto.CarDto;
import com.mourad.backend.application.port.in.CreateCarUseCase;
import com.mourad.backend.application.port.in.DeleteCarUseCase;
import com.mourad.backend.application.port.in.GetCarByIdUseCase;
import com.mourad.backend.application.port.in.GetCarsUseCase;
import com.mourad.backend.application.port.in.UpdateCarPriceUseCase;
import com.mourad.backend.application.port.in.UpdateCarUseCase;
import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.interfaces.dto.request.CreateCarRequest;
import com.mourad.backend.interfaces.dto.request.UpdateCarPriceRequest;
import com.mourad.backend.interfaces.dto.request.UpdateCarRequest;
import com.mourad.backend.interfaces.dto.response.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/cars")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Cars", description = "Fleet management — requires ADMIN role")
public class CarController {

    private final CreateCarUseCase createCarUseCase;
    private final DeleteCarUseCase deleteCarUseCase;
    private final UpdateCarUseCase updateCarUseCase;
    private final UpdateCarPriceUseCase updateCarPriceUseCase;
    private final GetCarsUseCase getCarsUseCase;
    private final GetCarByIdUseCase getCarByIdUseCase;

    public CarController(CreateCarUseCase createCarUseCase,
                         DeleteCarUseCase deleteCarUseCase,
                         UpdateCarUseCase updateCarUseCase,
                         UpdateCarPriceUseCase updateCarPriceUseCase,
                         GetCarsUseCase getCarsUseCase,
                         GetCarByIdUseCase getCarByIdUseCase) {
        this.createCarUseCase = createCarUseCase;
        this.deleteCarUseCase = deleteCarUseCase;
        this.updateCarUseCase = updateCarUseCase;
        this.updateCarPriceUseCase = updateCarPriceUseCase;
        this.getCarsUseCase = getCarsUseCase;
        this.getCarByIdUseCase = getCarByIdUseCase;
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    @PostMapping
    @Operation(
            summary = "Add a car to the fleet",
            description = "Creates a new car entry. The car is automatically set to `AVAILABLE`. Currency must be a valid ISO 4217 code."
    )
    @ApiResponse(responseCode = "201", description = "Car created — Location header points to the new resource",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CarDto.class),
                    examples = @ExampleObject(name = "created", value = """
                            {
                              "id": "550e8400-e29b-41d4-a716-446655440000",
                              "brand": "Peugeot",
                              "model": "308",
                              "year": 2023,
                              "dailyPrice": 55.00,
                              "currency": "EUR",
                              "status": "AVAILABLE",
                              "createdAt": "2026-02-25T10:00:00",
                              "updatedAt": "2026-02-25T10:00:00"
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Validation failure",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2026-02-25T20:00:00.000Z",
                              "status": 400,
                              "error": "BAD_REQUEST",
                              "message": "brand: Brand is required, dailyPrice: Daily price must be greater than 0",
                              "path": "/api/admin/cars"
                            }
                            """)))
    @ApiResponse(responseCode = "401", description = "Missing or invalid token",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2026-02-25T20:00:00.000Z",
                              "status": 401,
                              "error": "UNAUTHORIZED",
                              "message": "Authentication required",
                              "path": "/api/admin/cars"
                            }
                            """)))
    @ApiResponse(responseCode = "403", description = "Authenticated but not ADMIN",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2026-02-25T20:00:00.000Z",
                              "status": 403,
                              "error": "FORBIDDEN",
                              "message": "Access denied",
                              "path": "/api/admin/cars"
                            }
                            """)))
    @ApiResponse(responseCode = "422", description = "Domain invariant violated (invalid currency, price ≤ 0…)",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2026-02-25T20:00:00.000Z",
                              "status": 422,
                              "error": "UNPROCESSABLE_ENTITY",
                              "message": "Invalid ISO 4217 currency code: XYZ",
                              "path": "/api/admin/cars"
                            }
                            """)))
    public ResponseEntity<CarDto> createCar(@Valid @RequestBody CreateCarRequest request) {
        CarDto dto = createCarUseCase.execute(new CreateCarCommand(
                request.brand(),
                request.model(),
                request.year(),
                request.pricePerDay(),
                request.currency(),
                request.seats(),
                request.transmission(),
                request.fuel(),
                request.city(),
                request.imageUrl()
        ));
        return ResponseEntity
                .created(URI.create("/api/admin/cars/" + dto.id()))
                .body(dto);
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    @GetMapping
    @Operation(summary = "List all cars, optionally filtered by status")
    @ApiResponse(responseCode = "200", description = "Car list")
    @ApiResponse(responseCode = "401", description = "Missing or invalid token",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "403", description = "Authenticated but not ADMIN",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<List<CarDto>> getAllCars(
            @Parameter(description = "Filter by status — omit for all cars")
            @RequestParam(required = false) CarStatus status) {
        List<CarDto> cars = status != null
                ? getCarsUseCase.findByStatus(status)
                : getCarsUseCase.findAll();
        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a car by ID")
    @ApiResponse(responseCode = "200", description = "Car found")
    @ApiResponse(responseCode = "401", description = "Missing or invalid token",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "403", description = "Authenticated but not ADMIN",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Car not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<CarDto> getCarById(@PathVariable UUID id) {
        return ResponseEntity.ok(getCarByIdUseCase.execute(id));
    }

    // ── UPDATE (full) ─────────────────────────────────────────────────────────

    @PutMapping("/{id}")
    @Operation(summary = "Fully update a car")
    @ApiResponse(responseCode = "200", description = "Car updated",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = CarDto.class)))
    @ApiResponse(responseCode = "400", description = "Validation failure",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "401", description = "Missing or invalid token",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "403", description = "Authenticated but not ADMIN",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Car not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "422", description = "Domain invariant violated",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<CarDto> updateCar(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCarRequest request) {
        CarDto dto = updateCarUseCase.execute(id, new UpdateCarCommand(
                request.brand(),
                request.model(),
                request.year(),
                request.pricePerDay(),
                request.currency(),
                request.seats(),
                request.transmission(),
                request.fuel(),
                request.city(),
                request.imageUrl()
        ));
        return ResponseEntity.ok(dto);
    }

    // ── UPDATE (price only) ───────────────────────────────────────────────────

    @PatchMapping("/{id}/price")
    @Operation(
            summary = "Update the daily price of a car",
            description = "Replaces only the `dailyPrice` and `currency` fields. All domain invariants are re-validated."
    )
    @ApiResponse(responseCode = "200", description = "Price updated",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CarDto.class),
                    examples = @ExampleObject(name = "updated", value = """
                            {
                              "id": "550e8400-e29b-41d4-a716-446655440000",
                              "brand": "Peugeot",
                              "model": "308",
                              "year": 2023,
                              "dailyPrice": 60.00,
                              "currency": "EUR",
                              "status": "AVAILABLE",
                              "createdAt": "2026-02-25T10:00:00",
                              "updatedAt": "2026-02-25T12:00:00"
                            }
                            """)))
    @ApiResponse(responseCode = "400", description = "Validation failure",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2026-02-25T20:00:00.000Z",
                              "status": 400,
                              "error": "BAD_REQUEST",
                              "message": "dailyPrice: Daily price must be greater than 0",
                              "path": "/api/admin/cars/550e8400-e29b-41d4-a716-446655440000/price"
                            }
                            """)))
    @ApiResponse(responseCode = "401", description = "Missing or invalid token",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "403", description = "Authenticated but not ADMIN",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Car not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2026-02-25T20:00:00.000Z",
                              "status": 404,
                              "error": "NOT_FOUND",
                              "message": "Car not found with id: 550e8400-e29b-41d4-a716-446655440000",
                              "path": "/api/admin/cars/550e8400-e29b-41d4-a716-446655440000/price"
                            }
                            """)))
    @ApiResponse(responseCode = "422", description = "Domain invariant violated",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
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
    @ApiResponse(responseCode = "204", description = "Car deleted — no content")
    @ApiResponse(responseCode = "401", description = "Missing or invalid token",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "403", description = "Authenticated but not ADMIN",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Car not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> deleteCar(@PathVariable UUID id) {
        deleteCarUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}

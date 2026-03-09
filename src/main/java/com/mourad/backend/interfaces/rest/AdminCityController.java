package com.mourad.backend.interfaces.rest;

import com.mourad.backend.application.command.CreateCityCommand;
import com.mourad.backend.application.dto.CityDto;
import com.mourad.backend.application.port.in.CreateCityUseCase;
import com.mourad.backend.application.port.in.DeleteCityUseCase;
import com.mourad.backend.interfaces.dto.request.CreateCityRequest;
import com.mourad.backend.interfaces.dto.response.ApiError;
import io.swagger.v3.oas.annotations.Operation;
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
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/cities")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Cities", description = "City management — requires ADMIN role")
public class AdminCityController {

    private final CreateCityUseCase createCityUseCase;
    private final DeleteCityUseCase deleteCityUseCase;

    public AdminCityController(CreateCityUseCase createCityUseCase,
                               DeleteCityUseCase deleteCityUseCase) {
        this.createCityUseCase = createCityUseCase;
        this.deleteCityUseCase = deleteCityUseCase;
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    @PostMapping
    @Operation(summary = "Create a new city")
    @ApiResponse(responseCode = "201", description = "City created",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CityDto.class),
                    examples = @ExampleObject(value = """
                            {"id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890", "name": "Marrakech"}
                            """)))
    @ApiResponse(responseCode = "400", description = "Validation failure",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "401", description = "Missing or invalid token",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "403", description = "Authenticated but not ADMIN",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<CityDto> createCity(@Valid @RequestBody CreateCityRequest request) {
        CityDto dto = createCityUseCase.execute(new CreateCityCommand(request.name().strip()));
        return ResponseEntity
                .created(URI.create("/api/admin/cities/" + dto.id()))
                .body(dto);
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a city by ID")
    @ApiResponse(responseCode = "204", description = "City deleted — no content")
    @ApiResponse(responseCode = "401", description = "Missing or invalid token",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "403", description = "Authenticated but not ADMIN",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "404", description = "City not found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ApiError.class),
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2026-03-09T10:00:00.000Z",
                              "status": 404,
                              "error": "NOT_FOUND",
                              "message": "City not found with id: a1b2c3d4-...",
                              "path": "/api/admin/cities/a1b2c3d4-..."
                            }
                            """)))
    public ResponseEntity<Void> deleteCity(@PathVariable UUID id) {
        deleteCityUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}

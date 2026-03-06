package com.mourad.backend.interfaces.rest;

import com.mourad.backend.application.command.CreateUnavailablePeriodCommand;
import com.mourad.backend.application.dto.UnavailablePeriodDto;
import com.mourad.backend.application.port.in.AddUnavailablePeriodUseCase;
import com.mourad.backend.application.port.in.DeleteUnavailablePeriodUseCase;
import com.mourad.backend.application.port.in.GetUnavailablePeriodsUseCase;
import com.mourad.backend.interfaces.dto.request.CreateUnavailablePeriodRequest;
import com.mourad.backend.interfaces.dto.response.ApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Unavailable Periods", description = "Manage car unavailability windows — requires ADMIN role")
public class UnavailablePeriodController {

    private final AddUnavailablePeriodUseCase addUseCase;
    private final GetUnavailablePeriodsUseCase getUseCase;
    private final DeleteUnavailablePeriodUseCase deleteUseCase;

    public UnavailablePeriodController(AddUnavailablePeriodUseCase addUseCase,
                                        GetUnavailablePeriodsUseCase getUseCase,
                                        DeleteUnavailablePeriodUseCase deleteUseCase) {
        this.addUseCase = addUseCase;
        this.getUseCase = getUseCase;
        this.deleteUseCase = deleteUseCase;
    }

    @PostMapping("/api/admin/cars/{id}/unavailable-periods")
    @Operation(summary = "Add an unavailability period to a car")
    @ApiResponse(responseCode = "201", description = "Period created")
    @ApiResponse(responseCode = "400", description = "Validation failure",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "404", description = "Car not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    @ApiResponse(responseCode = "422", description = "startDate is after endDate",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<UnavailablePeriodDto> addPeriod(
            @PathVariable UUID id,
            @Valid @RequestBody CreateUnavailablePeriodRequest request) {
        UnavailablePeriodDto dto = addUseCase.execute(id, new CreateUnavailablePeriodCommand(
                request.startDate(),
                request.endDate(),
                request.reason()
        ));
        return ResponseEntity
                .created(URI.create("/api/admin/unavailable-periods/" + dto.id()))
                .body(dto);
    }

    @GetMapping("/api/admin/cars/{id}/unavailable-periods")
    @Operation(summary = "List all unavailability periods of a car")
    @ApiResponse(responseCode = "200", description = "Period list")
    public ResponseEntity<List<UnavailablePeriodDto>> getPeriods(@PathVariable UUID id) {
        return ResponseEntity.ok(getUseCase.findByCarId(id));
    }

    @DeleteMapping("/api/admin/unavailable-periods/{periodId}")
    @Operation(summary = "Remove an unavailability period")
    @ApiResponse(responseCode = "204", description = "Period deleted — no content")
    @ApiResponse(responseCode = "404", description = "Period not found",
            content = @Content(schema = @Schema(implementation = ApiError.class)))
    public ResponseEntity<Void> deletePeriod(@PathVariable UUID periodId) {
        deleteUseCase.execute(periodId);
        return ResponseEntity.noContent().build();
    }
}

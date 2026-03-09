package com.mourad.backend.interfaces.rest;

import com.mourad.backend.application.dto.CityDto;
import com.mourad.backend.application.port.in.GetCitiesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
@Tag(name = "Public Cities", description = "City list — no authentication required")
public class PublicCityController {

    private final GetCitiesUseCase getCitiesUseCase;

    public PublicCityController(GetCitiesUseCase getCitiesUseCase) {
        this.getCitiesUseCase = getCitiesUseCase;
    }

    @GetMapping
    @Operation(
            summary = "List all cities",
            description = "Returns the full list of cities available for car rental. No authentication required."
    )
    @ApiResponse(responseCode = "200", description = "City list",
            content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            [
                              {"id": "a1b2c3d4-...", "name": "Mohammadia"},
                              {"id": "b2c3d4e5-...", "name": "Rabat Aéroport"},
                              {"id": "c3d4e5f6-...", "name": "Rabat Centre Ville"},
                              {"id": "d4e5f6a7-...", "name": "Casablanca Aéroport"},
                              {"id": "e5f6a7b8-...", "name": "Casablanca Centre Ville"}
                            ]
                            """)))
    public ResponseEntity<List<CityDto>> getCities() {
        return ResponseEntity.ok(getCitiesUseCase.findAll());
    }
}

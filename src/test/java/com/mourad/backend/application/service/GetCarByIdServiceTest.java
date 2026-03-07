package com.mourad.backend.application.service;

import com.mourad.backend.application.dto.CarDto;
import com.mourad.backend.domain.exception.CarNotFoundException;
import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.port.out.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCarByIdServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private GetCarByIdService getCarByIdService;

    private static Car validCar() {
        return Car.create("Renault", "Clio", 2022, BigDecimal.valueOf(50), "EUR", null, null, null, null, null);
    }

    // ── Test 1 — found ────────────────────────────────────────────────────────

    @Test
    void execute_shouldReturnCarDto_whenCarExists() {
        UUID id = UUID.randomUUID();
        Car car = validCar();
        when(carRepository.findById(id)).thenReturn(Optional.of(car));

        CarDto result = getCarByIdService.execute(id);

        assertThat(result.brand()).isEqualTo("Renault");
        assertThat(result.model()).isEqualTo("Clio");
        verify(carRepository).findById(id);
    }

    // ── Test 2 — not found ────────────────────────────────────────────────────

    @Test
    void execute_shouldThrowCarNotFoundException_whenCarDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(carRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getCarByIdService.execute(id))
                .isInstanceOf(CarNotFoundException.class)
                .hasMessageContaining(id.toString());
    }
}

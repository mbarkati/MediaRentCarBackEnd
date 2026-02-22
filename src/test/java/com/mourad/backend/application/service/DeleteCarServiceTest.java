package com.mourad.backend.application.service;

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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private DeleteCarService deleteCarService;

    private static Car validCar() {
        return Car.create("Renault", "Clio", 2022, BigDecimal.valueOf(50), "EUR");
    }

    // ── Test 1 — happy path ───────────────────────────────────────────────────

    @Test
    void execute_shouldDeleteCar_whenCarExists() {
        UUID id = UUID.randomUUID();
        when(carRepository.findById(id)).thenReturn(Optional.of(validCar()));

        deleteCarService.execute(id);

        verify(carRepository).deleteById(id);
    }

    // ── Test 2 — car not found ────────────────────────────────────────────────

    @Test
    void execute_shouldThrowCarNotFoundException_whenCarDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(carRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteCarService.execute(id))
                .isInstanceOf(CarNotFoundException.class)
                .hasMessageContaining(id.toString());

        verify(carRepository, never()).deleteById(any());
    }
}

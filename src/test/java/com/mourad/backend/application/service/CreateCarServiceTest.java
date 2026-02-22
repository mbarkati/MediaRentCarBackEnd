package com.mourad.backend.application.service;

import com.mourad.backend.application.command.CreateCarCommand;
import com.mourad.backend.application.dto.CarDto;
import com.mourad.backend.domain.exception.InvalidCarStateException;
import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.domain.port.out.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CreateCarService createCarService;

    // ── Test 1 — happy path ───────────────────────────────────────────────────

    @Test
    void execute_shouldReturnCarDto_whenInputIsValid() {
        when(carRepository.save(any(Car.class))).thenAnswer(inv -> inv.getArgument(0));

        CreateCarCommand command = new CreateCarCommand("Renault", "Clio", 2022, BigDecimal.valueOf(50), "EUR");
        CarDto result = createCarService.execute(command);

        assertThat(result.brand()).isEqualTo("Renault");
        assertThat(result.model()).isEqualTo("Clio");
        assertThat(result.year()).isEqualTo(2022);
        assertThat(result.dailyPrice()).isEqualByComparingTo(BigDecimal.valueOf(50));
        assertThat(result.currency()).isEqualTo("EUR");
        assertThat(result.status()).isEqualTo(CarStatus.AVAILABLE);
        verify(carRepository).save(any(Car.class));
    }

    // ── Test 2 — domain rule: price must be > 0 ───────────────────────────────

    @Test
    void execute_shouldThrow_whenDailyPriceIsNotPositive() {
        CreateCarCommand command = new CreateCarCommand("Renault", "Clio", 2022, BigDecimal.ZERO, "EUR");

        assertThatThrownBy(() -> createCarService.execute(command))
                .isInstanceOf(InvalidCarStateException.class)
                .hasMessageContaining("Daily price");

        verify(carRepository, never()).save(any());
    }

    // ── Test 3 — domain rule: year in [1980, currentYear+1] ──────────────────

    @Test
    void execute_shouldThrow_whenYearIsOutOfRange() {
        CreateCarCommand command = new CreateCarCommand("Renault", "Clio", 1900, BigDecimal.valueOf(50), "EUR");

        assertThatThrownBy(() -> createCarService.execute(command))
                .isInstanceOf(InvalidCarStateException.class)
                .hasMessageContaining("year");

        verify(carRepository, never()).save(any());
    }
}

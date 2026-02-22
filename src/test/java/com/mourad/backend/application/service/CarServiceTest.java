package com.mourad.backend.application.service;

import com.mourad.backend.domain.exception.CarAlreadyExistsException;
import com.mourad.backend.domain.exception.CarNotFoundException;
import com.mourad.backend.domain.model.Car;
import com.mourad.backend.domain.model.CarStatus;
import com.mourad.backend.domain.model.Money;
import com.mourad.backend.domain.port.out.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    private static final Money DAILY_RATE = Money.of(BigDecimal.valueOf(50), "EUR");

    // --- Test 1 ---
    @Test
    void createCar_shouldReturnCreatedCar_whenLicensePlateIsUnique() {
        when(carRepository.existsByLicensePlate("AB-123-CD")).thenReturn(false);
        when(carRepository.save(any(Car.class))).thenAnswer(inv -> inv.getArgument(0));

        Car result = carService.createCar("AB-123-CD", "Renault", "Clio", DAILY_RATE);

        assertThat(result.getLicensePlate()).isEqualTo("AB-123-CD");
        assertThat(result.getBrand()).isEqualTo("Renault");
        assertThat(result.getModel()).isEqualTo("Clio");
        assertThat(result.getStatus()).isEqualTo(CarStatus.AVAILABLE);
        verify(carRepository).save(any(Car.class));
    }

    // --- Test 2 ---
    @Test
    void createCar_shouldThrowCarAlreadyExistsException_whenLicensePlateAlreadyExists() {
        when(carRepository.existsByLicensePlate("AB-123-CD")).thenReturn(true);

        assertThatThrownBy(() -> carService.createCar("AB-123-CD", "Renault", "Clio", DAILY_RATE))
                .isInstanceOf(CarAlreadyExistsException.class)
                .hasMessageContaining("AB-123-CD");

        verify(carRepository, never()).save(any());
    }

    // --- Test 3 ---
    @Test
    void getCarById_shouldReturnCar_whenCarExists() {
        UUID id = UUID.randomUUID();
        Car car = Car.create("AB-123-CD", "Renault", "Clio", DAILY_RATE);
        when(carRepository.findById(id)).thenReturn(Optional.of(car));

        Car result = carService.getCarById(id);

        assertThat(result).isSameAs(car);
    }

    // --- Test 4 ---
    @Test
    void getCarById_shouldThrowCarNotFoundException_whenCarDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(carRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> carService.getCarById(id))
                .isInstanceOf(CarNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    // --- Test 5 ---
    @Test
    void getAllCars_shouldReturnAllCars() {
        List<Car> cars = List.of(
                Car.create("AB-123-CD", "Renault", "Clio", DAILY_RATE),
                Car.create("EF-456-GH", "Peugeot", "208", DAILY_RATE)
        );
        when(carRepository.findAll()).thenReturn(cars);

        List<Car> result = carService.getAllCars();

        assertThat(result).hasSize(2);
        verify(carRepository).findAll();
    }

    // --- Test 6 ---
    @Test
    void updateCar_shouldReturnUpdatedCar_whenCarExists() {
        UUID id = UUID.randomUUID();
        Car car = Car.create("AB-123-CD", "Renault", "Clio", DAILY_RATE);
        Money newRate = Money.of(BigDecimal.valueOf(75), "EUR");
        when(carRepository.findById(id)).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenAnswer(inv -> inv.getArgument(0));

        Car result = carService.updateCar(id, "Renault", "Megane", newRate);

        assertThat(result.getModel()).isEqualTo("Megane");
        assertThat(result.getDailyRate().amount()).isEqualByComparingTo(BigDecimal.valueOf(75));
        verify(carRepository).save(car);
    }

    // --- Test 7 ---
    @Test
    void changeCarStatus_shouldUpdateStatus_whenCarExists() {
        UUID id = UUID.randomUUID();
        Car car = Car.create("AB-123-CD", "Renault", "Clio", DAILY_RATE);
        when(carRepository.findById(id)).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenAnswer(inv -> inv.getArgument(0));

        Car result = carService.changeCarStatus(id, CarStatus.RENTED);

        assertThat(result.getStatus()).isEqualTo(CarStatus.RENTED);
        verify(carRepository).save(car);
    }
}

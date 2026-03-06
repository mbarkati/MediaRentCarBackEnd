package com.mourad.backend.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "unavailable_periods")
public class UnavailablePeriodJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private CarJpaEntity car;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column
    private String reason;

    public UnavailablePeriodJpaEntity() {}

    public UUID getId() { return id; }
    public CarJpaEntity getCar() { return car; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getReason() { return reason; }

    public void setId(UUID id) { this.id = id; }
    public void setCar(CarJpaEntity car) { this.car = car; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setReason(String reason) { this.reason = reason; }
}

package com.mourad.backend.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class City {

    private UUID id;
    private String name;
    private LocalDateTime createdAt;

    private City() {}

    public static City create(String name) {
        City city = new City();
        city.id = UUID.randomUUID();
        city.name = name;
        city.createdAt = LocalDateTime.now();
        return city;
    }

    public static City reconstitute(UUID id, String name, LocalDateTime createdAt) {
        City city = new City();
        city.id = id;
        city.name = name;
        city.createdAt = createdAt;
        return city;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

package com.mourad.backend.infrastructure.persistence.mapper;

import com.mourad.backend.domain.model.City;
import com.mourad.backend.infrastructure.persistence.entity.CityJpaEntity;

public class CityMapper {

    private CityMapper() {}

    public static City toDomain(CityJpaEntity entity) {
        return City.reconstitute(entity.getId(), entity.getName(), entity.getCreatedAt());
    }

    public static CityJpaEntity toEntity(City city) {
        CityJpaEntity entity = new CityJpaEntity();
        entity.setId(city.getId());
        entity.setName(city.getName());
        entity.setCreatedAt(city.getCreatedAt());
        return entity;
    }
}

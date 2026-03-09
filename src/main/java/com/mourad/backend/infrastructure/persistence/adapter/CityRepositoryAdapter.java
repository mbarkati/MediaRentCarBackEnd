package com.mourad.backend.infrastructure.persistence.adapter;

import com.mourad.backend.domain.model.City;
import com.mourad.backend.domain.port.out.CityRepository;
import com.mourad.backend.infrastructure.persistence.mapper.CityMapper;
import com.mourad.backend.infrastructure.persistence.repository.CityJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class CityRepositoryAdapter implements CityRepository {

    private final CityJpaRepository jpaRepository;

    public CityRepositoryAdapter(CityJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public City save(City city) {
        return CityMapper.toDomain(jpaRepository.save(CityMapper.toEntity(city)));
    }

    @Override
    public List<City> findAll() {
        return jpaRepository.findAll().stream().map(CityMapper::toDomain).toList();
    }

    @Override
    public Optional<City> findById(UUID id) {
        return jpaRepository.findById(id).map(CityMapper::toDomain);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean isEmpty() {
        return jpaRepository.count() == 0;
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}

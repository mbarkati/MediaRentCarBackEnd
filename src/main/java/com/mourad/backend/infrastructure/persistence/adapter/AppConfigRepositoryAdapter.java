package com.mourad.backend.infrastructure.persistence.adapter;

import com.mourad.backend.domain.model.AppConfig;
import com.mourad.backend.domain.port.out.AppConfigRepository;
import com.mourad.backend.infrastructure.persistence.entity.AppConfigJpaEntity;
import com.mourad.backend.infrastructure.persistence.repository.AppConfigJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AppConfigRepositoryAdapter implements AppConfigRepository {

    private final AppConfigJpaRepository jpaRepository;

    public AppConfigRepositoryAdapter(AppConfigJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<AppConfig> findByKey(String key) {
        return jpaRepository.findById(key)
                .map(e -> AppConfig.reconstitute(e.getKey(), e.getValue()));
    }

    @Override
    public AppConfig save(AppConfig config) {
        AppConfigJpaEntity entity = new AppConfigJpaEntity();
        entity.setKey(config.getKey());
        entity.setValue(config.getValue());
        AppConfigJpaEntity saved = jpaRepository.save(entity);
        return AppConfig.reconstitute(saved.getKey(), saved.getValue());
    }
}

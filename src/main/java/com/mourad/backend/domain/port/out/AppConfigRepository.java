package com.mourad.backend.domain.port.out;

import com.mourad.backend.domain.model.AppConfig;

import java.util.Optional;

public interface AppConfigRepository {

    Optional<AppConfig> findByKey(String key);

    AppConfig save(AppConfig config);
}

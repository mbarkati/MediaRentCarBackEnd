package com.mourad.backend.infrastructure.persistence.repository;

import com.mourad.backend.infrastructure.persistence.entity.AppConfigJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppConfigJpaRepository extends JpaRepository<AppConfigJpaEntity, String> {}

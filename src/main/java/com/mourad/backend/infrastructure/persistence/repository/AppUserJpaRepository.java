package com.mourad.backend.infrastructure.persistence.repository;

import com.mourad.backend.infrastructure.persistence.entity.AppUserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface AppUserJpaRepository extends JpaRepository<AppUserJpaEntity, UUID> {

    boolean existsByPhone(String phone);

    Optional<AppUserJpaEntity> findByPhone(String phone);

    @Transactional
    void deleteByPhone(String phone);
}

package com.mourad.backend.infrastructure.persistence.repository;

import com.mourad.backend.infrastructure.persistence.entity.AdminAccountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdminJpaRepository extends JpaRepository<AdminAccountJpaEntity, UUID> {

    Optional<AdminAccountJpaEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}

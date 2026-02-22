package com.mourad.backend.infrastructure.persistence.adapter;

import com.mourad.backend.domain.model.AdminAccount;
import com.mourad.backend.domain.port.out.AdminAccountRepository;
import com.mourad.backend.infrastructure.persistence.mapper.AdminMapper;
import com.mourad.backend.infrastructure.persistence.repository.AdminJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AdminRepositoryAdapter implements AdminAccountRepository {

    private final AdminJpaRepository jpaRepository;

    public AdminRepositoryAdapter(AdminJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<AdminAccount> findByUsername(String username) {
        return jpaRepository.findByUsername(username).map(AdminMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    public AdminAccount save(AdminAccount adminAccount) {
        return AdminMapper.toDomain(jpaRepository.save(AdminMapper.toEntity(adminAccount)));
    }
}

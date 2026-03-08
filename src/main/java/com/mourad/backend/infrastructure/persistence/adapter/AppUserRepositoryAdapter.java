package com.mourad.backend.infrastructure.persistence.adapter;

import com.mourad.backend.domain.model.AppUser;
import com.mourad.backend.domain.port.out.AppUserRepository;
import com.mourad.backend.infrastructure.persistence.mapper.AppUserMapper;
import com.mourad.backend.infrastructure.persistence.repository.AppUserJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AppUserRepositoryAdapter implements AppUserRepository {

    private final AppUserJpaRepository jpaRepository;

    public AppUserRepositoryAdapter(AppUserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean existsByPhone(String phone) {
        return jpaRepository.existsByPhone(phone);
    }

    @Override
    public AppUser save(AppUser user) {
        return AppUserMapper.toDomain(jpaRepository.save(AppUserMapper.toEntity(user)));
    }
}

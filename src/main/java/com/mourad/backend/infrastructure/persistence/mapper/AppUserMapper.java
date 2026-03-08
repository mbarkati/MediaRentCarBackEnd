package com.mourad.backend.infrastructure.persistence.mapper;

import com.mourad.backend.domain.model.AppUser;
import com.mourad.backend.infrastructure.persistence.entity.AppUserJpaEntity;

public class AppUserMapper {

    private AppUserMapper() {}

    public static AppUser toDomain(AppUserJpaEntity entity) {
        return AppUser.reconstitute(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhone(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }

    public static AppUserJpaEntity toEntity(AppUser user) {
        AppUserJpaEntity entity = new AppUserJpaEntity();
        entity.setId(user.getId());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setPhone(user.getPhone());
        entity.setStatus(user.getStatus());
        entity.setCreatedAt(user.getCreatedAt());
        return entity;
    }
}

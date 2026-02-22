package com.mourad.backend.infrastructure.persistence.mapper;

import com.mourad.backend.domain.model.AdminAccount;
import com.mourad.backend.infrastructure.persistence.entity.AdminAccountJpaEntity;

public class AdminMapper {

    private AdminMapper() {}

    public static AdminAccount toDomain(AdminAccountJpaEntity entity) {
        return AdminAccount.reconstitute(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getCreatedAt()
        );
    }

    public static AdminAccountJpaEntity toEntity(AdminAccount account) {
        AdminAccountJpaEntity entity = new AdminAccountJpaEntity();
        entity.setId(account.getId());
        entity.setUsername(account.getUsername());
        entity.setPassword(account.getHashedPassword());
        entity.setCreatedAt(account.getCreatedAt());
        return entity;
    }
}

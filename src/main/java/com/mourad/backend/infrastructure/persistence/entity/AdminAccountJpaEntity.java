package com.mourad.backend.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admin_accounts")
public class AdminAccountJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public AdminAccountJpaEntity() {}

    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(UUID id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

package com.mourad.backend.infrastructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_config")
public class AppConfigJpaEntity {

    @Id
    @Column(name = "config_key", length = 100)
    private String key;

    @Column(name = "config_value", nullable = false, length = 500)
    private String value;

    public AppConfigJpaEntity() {}

    public String getKey() { return key; }
    public String getValue() { return value; }

    public void setKey(String key) { this.key = key; }
    public void setValue(String value) { this.value = value; }
}

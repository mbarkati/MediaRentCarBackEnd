package com.mourad.backend.domain.model;

public class AppConfig {

    private String key;
    private String value;

    private AppConfig() {}

    public static AppConfig create(String key, String value) {
        AppConfig config = new AppConfig();
        config.key = key;
        config.value = value;
        return config;
    }

    public static AppConfig reconstitute(String key, String value) {
        AppConfig config = new AppConfig();
        config.key = key;
        config.value = value;
        return config;
    }

    public void updateValue(String value) {
        this.value = value;
    }

    public String getKey() { return key; }
    public String getValue() { return value; }
}

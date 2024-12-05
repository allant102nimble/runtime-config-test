package com.example.runtime_config.config;

public interface RefreshableProperties {
    void validate(String key, String value) throws RefreshablePropertyValidationException;
}

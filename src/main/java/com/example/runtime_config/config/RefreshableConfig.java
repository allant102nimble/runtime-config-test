package com.example.runtime_config.config;

public interface RefreshableConfig {
    void refreshBean();
    boolean requireRefresh();
}

package com.example.runtime_config.config;

public interface RefreshableConfig {
    public void refreshBean();
    public boolean requireRefresh();
}

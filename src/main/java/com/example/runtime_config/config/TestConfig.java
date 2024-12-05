package com.example.runtime_config.config;

import com.example.runtime_config.RandomBean;
import com.example.runtime_config.RandomBean2;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RefreshableProperties.class)
@AllArgsConstructor
@ComponentScan("com.example.runtime_config")
public class TestConfig {

    @Autowired
    ApplicationContext applicationContext;

    private RefreshableProperties refreshableProperties;

    @Bean
    @RefreshScope
    public RandomBean randomBean() {
        return new RandomBean(refreshableProperties.getProperty1());
    }

}

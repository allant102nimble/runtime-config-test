package com.example.runtime_config.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@ConfigurationProperties("hello")
@Getter
@Setter
@RefreshScope
public class TestProperties implements RefreshableProperties {
    Integer property1;

    @Override
    public void validate(String key, String value) throws RefreshablePropertyValidationException {
        return;
    }

}

package com.example.runtime_config.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@ConfigurationProperties
@Getter
@Setter
@RefreshScope
public class TestProperties {

    String property1;

}

package com.example.runtime_config.config;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class TestMessage {

    private String configType;
    private String configValue;
}

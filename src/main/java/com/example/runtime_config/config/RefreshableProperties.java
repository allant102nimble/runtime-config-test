package com.example.runtime_config.config;

import com.example.runtime_config.RandomBean;
import com.example.runtime_config.RandomBean2;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@ConfigurationProperties("hello")
@Getter
@Setter
@RefreshScope
public class RefreshableProperties {
    String property1;

    public void validate(String key, String value) throws RefreshablePropertyValidationException {
        return;
    }

    public static ImmutableMap<String, List<Class>> DEPENDENT_BEANS = ImmutableMap.of(
        "property1", List.of(RandomBean.class, RandomBean2.class)
    );
}

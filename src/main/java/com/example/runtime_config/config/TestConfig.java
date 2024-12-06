package com.example.runtime_config.config;

import com.example.runtime_config.RandomBean;
import com.example.runtime_config.RandomBean2;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
import org.springframework.context.annotation.Fallback;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties(TestProperties.class)
@AllArgsConstructor
@ComponentScan("com.example.runtime_config")
public class TestConfig implements RefreshableConfig {

    @Autowired
    ApplicationContext applicationContext;

    private TestProperties testProperties;


    @Bean
    @RefreshScope
    public RandomBean randomBean() {
        try {
            return new RandomBean(testProperties.getProperty1());
        } catch (Exception e) {
            return fallbackRandomBean();
        }
    }

    // problem with this is that once the engineer resets the config value back to normal, there are two
    // randombeans
    @Bean
    @Fallback
    @Lazy
    public RandomBean fallbackRandomBean() {
        return new RandomBean(123);
    }

    public void refreshBean() {
        org.springframework.cloud.context.scope.refresh.RefreshScope refreshScope = applicationContext.getBean(org.springframework.cloud.context.scope.refresh.RefreshScope.class);
        refreshScope.refresh(TestProperties.class);
        refreshScope.refresh(RandomBean.class);
        refreshScope.refresh(RandomBean2.class);

//        List<Field> test = Arrays.stream(TestProperties.class.getDeclaredFields()).collect(Collectors.toList());
//        ConfigurationProperties cp = TestProperties.class.getAnnotation(ConfigurationProperties.class);
//        String test3 = cp.value();
//        String test2 = test.getFirst().getName();

//        // how you could update log level here...
//        TestProperties testProperties = applicationContext.getBean(TestProperties.class);
//        System.out.println(testProperties.getProperty1());

        // order of these calls matter because if one bean is being used and we call refresh, it blocks until it completes.

        // if we dont want one to block the other, we can run in parallel
//        CompletableFuture<Void> refresh1 = CompletableFuture.runAsync(() -> refreshScope.refresh(RandomBean.class));
//        CompletableFuture<Void> refresh2 = CompletableFuture.runAsync(() -> refreshScope.refresh(RandomBean2.class));
//
//        CompletableFuture.allOf(refresh1, refresh2).join();
    }

    public boolean requireRefresh() {
        return false;
    }
}

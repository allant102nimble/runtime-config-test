package com.example.runtime_config;

import com.example.runtime_config.config.RefreshableBean;
import com.example.runtime_config.config.RefreshableProperties;
import com.example.runtime_config.config.TestProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@RefreshScope
public class RandomBean2 implements RefreshableBean {

    @Autowired
    private ApplicationContext applicationContext;

    private TestProperties testProperties;
    private String property;

    public RandomBean2(TestProperties testProperties) {
        System.out.println("RandomBean2: new instance");
        this.property = testProperties.getProperty1();
        this.testProperties = testProperties;
    }

    public String bye() {
        try{
            Thread.sleep(3000);
        } catch (Exception e) {

        }
        return "RandomBean2: Property " + property;
    }

    public RefreshableProperties getRefreshableProperties() {
        return testProperties;
    }
}

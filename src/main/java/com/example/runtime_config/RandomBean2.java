package com.example.runtime_config;

import com.example.runtime_config.config.TestProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;

@Service
@RefreshScope
public class RandomBean2 {
    @Autowired
    private RandomBean randomBean;

    @Autowired
    private ApplicationContext applicationContext;
    private String property;


    public RandomBean2(TestProperties testProperties) throws BindException {
        System.out.println("RandomBean2: new instance");
        this.property = testProperties.getProperty1();
    }

    public String bye() {
        String[] dependentBeanNames = ((ConfigurableApplicationContext)applicationContext).getBeanFactory().getDependenciesForBean(TestProperties.class.getName());
        System.out.println(dependentBeanNames);
        try{
            Thread.sleep(3000);
        } catch (Exception e) {

        }
        System.out.println(randomBean.hello());
        return "RandomBean2: Property " + property;
    }
}

package com.example.runtime_config.controller;

import com.example.runtime_config.RandomBean;
import com.example.runtime_config.config.RedisConfiguration.RedisPublisher;
import com.example.runtime_config.config.RefreshableConfig;
import com.example.runtime_config.config.RefreshableProperties;
import com.example.runtime_config.config.TestProperties;
import com.example.runtime_config.service.TestService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class TestController {

    @Autowired
    private TestService testService;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private List<RefreshableConfig> refreshableConfigurations;

    @Autowired
    private List<RefreshableProperties> refreshableProperties;

    @Autowired
    private RedisPublisher redisPublisher;

    @RequestMapping("/")
    public String home() {
        return "Hello World!";
    }

    @RequestMapping("/refresh")
    public String refreshConfig() {
        try {
            org.springframework.cloud.context.scope.refresh.RefreshScope refreshScope = applicationContext.getBean(org.springframework.cloud.context.scope.refresh.RefreshScope.class);
            refreshScope.refresh(refreshableProperties.getFirst().getClass());
            refreshScope.refresh(RandomBean.class);

            applicationContext.getBean(RandomBean.class);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "Refresh triggered";
    }

    @RequestMapping("/work")
    public String work() {
        return testService.doSomeWork();
    }

    @RequestMapping("/work2")
    public String work2() {
        return testService.doSomeWork2();
    }

    @RequestMapping("/set")
    public String setProperty() {
        System.setProperty("hello.property1", "updated_property_1");
//        System.out.println(getBeansWithRefreshScope());
        return "set";
    }

    @RequestMapping("/set2")
    public String setProperty2() {
        System.setProperty("hello.property1", "100");
        System.out.println(getBeansWithRefreshScope());
        return "set";
    }



    @RequestMapping("/publish")
    public String publish() {
        redisPublisher.publish("test-channel", "{\"configType\": \"test\", \"configValue\": \"hello\"}");
        return "published";
    }

    private List<String> getBeansWithRefreshScope() {
        ConfigurableApplicationContext configurableApplicationContext =
            (ConfigurableApplicationContext) applicationContext;

        List<String> refreshScopedBeans = new ArrayList<>();
        String[] beanNames = configurableApplicationContext.getBeanDefinitionNames();

        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = configurableApplicationContext.getBeanFactory().getBeanDefinition(beanName);
            if ("refresh".equals(beanDefinition.getScope())) {
                System.out.println(beanDefinition);
                refreshScopedBeans.add(beanDefinition.getBeanClassName());
            }
        }

        return refreshScopedBeans;
    }
}

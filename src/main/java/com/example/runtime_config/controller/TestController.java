package com.example.runtime_config.controller;

import static com.example.runtime_config.config.RefreshableProperties.DEPENDENT_BEANS;

import com.example.runtime_config.RandomBean;
import com.example.runtime_config.config.Message;
import com.example.runtime_config.config.Message.MessageBuilder;
import com.example.runtime_config.config.RedisConfiguration.RedisPublisher;
import com.example.runtime_config.config.RefreshableProperties;
import com.example.runtime_config.service.TestService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Lettuce.Cluster.Refresh;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.util.Pair;
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
    private RefreshableProperties refreshableProperties;

    @Autowired
    private RedisPublisher redisPublisher;

    @RequestMapping("/")
    public String home() {
        return "Hello World!";
    }

    @RequestMapping("/refresh")
    public String refreshConfig() {
        org.springframework.cloud.context.scope.refresh.RefreshScope refreshScope = applicationContext.getBean(org.springframework.cloud.context.scope.refresh.RefreshScope.class);
        refreshScope.refresh(RefreshableProperties.class);

        for (Map.Entry<String, List<Class>> entry : DEPENDENT_BEANS.entrySet()) {
            for (Class clazz : entry.getValue()) {
                refreshScope.refresh(clazz);
            }
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
        System.out.println(getBeansWithRefreshScope());
        return "set";
    }

    @RequestMapping("/publish")
    public String publish() {
        Message message = MessageBuilder.forType("CHANGE")
            .withAttribute("configType", "test")
            .withAttribute("configValue", "hello")
            .build();
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

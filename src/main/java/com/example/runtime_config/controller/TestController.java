package com.example.runtime_config.controller;

import com.example.runtime_config.config.RefreshableBean;
import com.example.runtime_config.config.RefreshableConfig;
import com.example.runtime_config.config.TestProperties;
import com.example.runtime_config.service.TestService;
import java.util.ArrayList;
import java.util.List;
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
    private List<RefreshableConfig> refreshableConfigs;

    @Autowired
    private List<RefreshableBean> refreshableBeans;

    @RequestMapping("/")
    public String home() {
        return "Hello World!";
    }

    @RequestMapping("/refresh")
    public String refreshConfig() {
        org.springframework.cloud.context.scope.refresh.RefreshScope refreshScope = applicationContext.getBean(org.springframework.cloud.context.scope.refresh.RefreshScope.class);
        for (RefreshableConfig refreshable : refreshableConfigs) {
            refreshable.refreshBean();
        }
//        for (RefreshableBean bean : refreshableBeans) {
//            refreshScope.refresh(bean.getRefreshableProperties().getClass());
//            refreshScope.refresh(bean.getClass());
//        }
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
        System.setProperty("property1", "updated_property_1");
        getRefreshScopeBeans();
        return "set";
    }

    private List<String> getRefreshScopeBeans() {
        ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
        String[] beanNames = context.getBeanDefinitionNames();
        List<String> refreshScopeBeans = new ArrayList<>();

        for (String beanName : beanNames) {
            BeanDefinition beanDefinition = context.getBeanFactory().getBeanDefinition(beanName);
            if (beanDefinition.getScope().equals("refresh")) {
                refreshScopeBeans.add(beanName);
            }
        }
        System.out.println(refreshScopeBeans);
        return refreshScopeBeans;
    }
}

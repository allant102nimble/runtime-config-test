package com.example.runtime_config.controller;

import com.example.runtime_config.config.RefreshableConfig;
import com.example.runtime_config.service.TestService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
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
    private List<RefreshableConfig> refreshables;

    @RequestMapping("/")
    public String home() {
        return "Hello World!";
    }

    @RequestMapping("/refresh")
    public String refreshConfig() {
        for (RefreshableConfig refreshable : refreshables) {
//            if (refreshable.requireRefresh())
            refreshable.refreshBean();
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
        System.setProperty("property1", "updated_property_1");
        return "set";
    }
}

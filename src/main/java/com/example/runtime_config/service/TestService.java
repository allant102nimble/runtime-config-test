package com.example.runtime_config.service;

import com.example.runtime_config.RandomBean;
import com.example.runtime_config.RandomBean2;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    private ImmutableMap<String, String> originalValues;

    @Autowired
    private RandomBean randomBean;

    @Autowired
    private RandomBean2 randomBean2;

    public String doSomeWork() {
        originalValues = ImmutableMap.of("hi", "bye");
        System.out.println(originalValues.get("hi"));
        return randomBean.hello();
    }

    public String doSomeWork2() {
        return randomBean2.bye();
    }
}

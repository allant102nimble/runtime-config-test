package com.example.runtime_config;

import jakarta.annotation.PostConstruct;

public class RandomBean {
    private Integer property;

    public RandomBean(Integer property) {
        System.out.println("RandomBean: new instance");
        this.property = property;
    }

    public String hello() {
//        try{
//            Thread.sleep(3000);
//        } catch (Exception e) {
//
//        }
        return "RandomBean: Property " + property;
    }

}

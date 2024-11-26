package com.example.runtime_config;

public class RandomBean {
    private String property;

    public RandomBean(String property) {
        System.out.println("RandomBean: new instance");
        this.property = property;
    }

    public String hello() {
        try{
            Thread.sleep(3000);
        } catch (Exception e) {

        }
        return "RandomBean: Property " + property;
    }
}

package com.example.jianglei.asyncsqlite.model.bean;

/**
 * Created by jianglei on 2016/4/29.
 */
public class Info {

    private String name;

    private String age;

    public Info(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}

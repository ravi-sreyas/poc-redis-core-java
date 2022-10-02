package com.ravisreyas.redis.vo;

import java.io.Serializable;

public class Customer implements Serializable {
    private static final long serialVersionUID = 4879099103900287860L;

    private Integer id;
    private String name;
    private Integer age;

    public Customer(Integer id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Customer(Object id, Object name, Object age) {
        this.id = Integer.parseInt(String.valueOf(id));
        this.name = String.valueOf(name);
        this.age = Integer.parseInt(String.valueOf(age));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

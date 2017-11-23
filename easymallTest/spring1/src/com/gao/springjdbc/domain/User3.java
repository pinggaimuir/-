package com.gao.springjdbc.domain;

import org.springframework.stereotype.Component;

/**
 * Created by tarena on 2016/9/24.
 */
@Component
public class User3 {
    private int  id;
    private String name;
    private int salary;
    private String sex;

    @Override
    public String toString() {
        return "User3{" +
                "salary=" + salary +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}

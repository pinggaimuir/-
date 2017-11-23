package com.gao.mvc1.domain;
import java.util.Date;

/**
 * Created by tarena on 2016/9/28.
 */

public class User11 {
    private String username;
    private int age;
    private String password;
    private String sex;
    private Date birthday;
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public User11(){}
    @Override
    public String toString() {
        return "User11{" +
                "age=" + age +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }

    public User11(String username, int age, String password, String sex) {
        this.username = username;
        this.age = age;
        this.password = password;
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

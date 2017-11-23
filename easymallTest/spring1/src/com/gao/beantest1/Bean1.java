package com.gao.beantest1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Properties;
/**
 * Created by tarena on 2016/9/18.
 */
@Component
public class Bean1 {
    public Bean1(){

    }

    public Bean1(Date date, int id, String username, String password) {
        this.date = date;
        this.id = id;
        this.username = username;
        this.password = password;

    }
    private Properties prop;
    private int id;
    @Value("${time}")
    private Date date;
    @Override
    public String toString() {
        return "Bean1{" +
                "date=" + date +
                ", prop=" + prop +
                ", id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }



    public Properties getProp() {
        return prop;
    }

    public void setProp(Properties prop) {
        this.prop = prop;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    private String password;
}

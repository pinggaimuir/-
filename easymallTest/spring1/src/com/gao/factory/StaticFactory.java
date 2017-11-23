package com.gao.factory;

import java.util.Calendar;

/**
 * 通过静态工厂创建董涛
 * Created by tarena on 2016/9/19.
 */
public class StaticFactory {
    public StaticFactory(){
        System.out.println("这是静态工厂");
    }
    //必须有static
    public static Calendar getTime(){
        return Calendar.getInstance();
    }
}

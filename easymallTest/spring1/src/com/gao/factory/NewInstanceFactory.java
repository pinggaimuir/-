package com.gao.factory;

import java.util.Calendar;

/**
 * Created by tarena on 2016/9/19.
 */
public class NewInstanceFactory {
    public NewInstanceFactory(){

    }
    public Calendar getTime(){
        return Calendar.getInstance();
    }
}

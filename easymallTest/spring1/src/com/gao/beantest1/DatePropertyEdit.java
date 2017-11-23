package com.gao.beantest1;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 属性编辑器
 * Created by tarena on 2016/9/19.
 */
public class DatePropertyEdit extends PropertyEditorSupport {
//    public String pattern;
//    public void setPattern(String pattern){
//        this.pattern=pattern;
//    }
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Date date= null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(text);
            this.setValue(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}

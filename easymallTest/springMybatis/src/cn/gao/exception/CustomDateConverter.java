package cn.gao.exception;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

;
/**
 * 日期格式转换器
 * Created by tarena on 2016/10/8.
 */
public class CustomDateConverter implements Converter<String,Date> {
    public Date convert(String s) {
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

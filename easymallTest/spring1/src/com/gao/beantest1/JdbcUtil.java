package com.gao.beantest1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by tarena on 2016/9/20.
 */
@Component("dbutil")
public class JdbcUtil {
    @Value("${url}")
    private String conn;
    public String getCon(){
        return conn;
    }
}

package com.gao.factory;

import com.gao.beantest1.Bean1;
import org.springframework.beans.factory.FactoryBean;

import java.util.Calendar;

/**
 * spring工厂 要实现FactoryBean接口 自动调用getObject 方法
 * Created by tarena on 2016/9/19.
 */
public class SpringFactory implements FactoryBean<Bean1> {
    public SpringFactory(){

    }
    @Override
    public Bean1 getObject() throws Exception {
        return new Bean1();
    }

    @Override
    public Class<?> getObjectType() {
        return Calendar.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}

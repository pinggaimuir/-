package com.gao.staticaop.aop1;

import org.springframework.stereotype.Component;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;

/**
 *
 * Created by tarena on 2016/9/21.
 */
@Component("introspector1")
public class Introspector1<T> {
//    private Class<T> clazz;
//    private Introspector1(Class<T> clazz){
//        this.clazz=clazz;
//    }
    public void getBenaIfo(Class<T> clazz){
        try {
            BeanInfo bi= Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] props=bi.getPropertyDescriptors();
            for(PropertyDescriptor prop:props){
                System.out.println(prop.getName());
            }
            MethodDescriptor[] methods=bi.getMethodDescriptors();
            for(MethodDescriptor method:methods ){
                System.out.println(method.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package test2;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;

/**
 * Created by tarena on 2016/9/19.
 */
public class Introspector1<T> {
    private String className;
    public Introspector1(String className){this.className=className;}
    public T handler(ResultSet rs){
        try {
            Class clazz=Class.forName(className);
            try {
                T t=(T)clazz.newInstance();
                BeanInfo bi= Introspector.getBeanInfo(clazz);
                PropertyDescriptor[] pds=bi.getPropertyDescriptors();
                for(PropertyDescriptor pd:pds ){
                    String Property=pd.getName();
                    Method setMethod=pd.getWriteMethod();
                    try {
                        setMethod.invoke(t,rs.getObject(Property));
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
 }

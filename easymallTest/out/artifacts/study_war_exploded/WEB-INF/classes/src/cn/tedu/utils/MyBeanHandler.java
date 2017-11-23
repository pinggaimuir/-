package cn.tedu.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by tarena on 2016/9/5.
 */
public class MyBeanHandler<T> implements MyResultSetHandler<T>{
    private Class<T> clz;
    public MyBeanHandler(Class<T> clz){
        this.clz=clz;
    }
    public T handler(ResultSet rs) throws Exception {
        while(rs.next()){
            //创建rs对应的类的对象
            T t=clz.newInstance();
            //用内省获得bean的信息
            BeanInfo bi= Introspector.getBeanInfo(clz);
            //获得bean的属性
            PropertyDescriptor[] pds=bi.getPropertyDescriptors();
            for(PropertyDescriptor pd:pds){
                //获得属性的名称
                String name=pd.getName();
                //反射获得属性的setter方法
                Method mtd=pd.getWriteMethod();
                try {//排除实体中的和属性名称在对应的结果集中找不到的情况
                    mtd.invoke(t, rs.getObject(name));
                }catch(SQLException e){
                    continue;
                }
            }
            return t;
        }
        return null;
    }
}

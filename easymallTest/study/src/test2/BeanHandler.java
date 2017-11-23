package test2;

import cn.tedu.utils.MyResultSetHandler;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by tarena on 2016/9/5.
 */
public class BeanHandler<T> implements MyResultSetHandler {
    private Class<T> clz;
    T t;
    public Object handler(ResultSet rs) throws Exception {
        if(rs.next()){
             t=clz.newInstance();
            /*通过内省获得bean中的信息*/
            BeanInfo bi= Introspector.getBeanInfo(clz);
            PropertyDescriptor[] pds=bi.getPropertyDescriptors();
            for(PropertyDescriptor pd:pds){
                /*获得属性的名称*/
                String name=pd.getName();
                /*获得属性所对应的setter方法*/
                Method mtd=pd.getWriteMethod();
                /*反射 */
                try {
                    mtd.invoke(t, rs.getObject(name));
                }catch(SQLException e){
                    throw new RuntimeException(e);
                }
            }
        }
        return t;
    }
}

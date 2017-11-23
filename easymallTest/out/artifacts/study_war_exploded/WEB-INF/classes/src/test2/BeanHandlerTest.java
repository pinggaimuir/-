package test2;

import org.apache.commons.dbutils.ResultSetHandler;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by tarena on 2016/9/6.
 */
public class BeanHandlerTest<T> implements ResultSetHandler<T> {
    private Class<T> clz;
    public BeanHandlerTest(Class<T> clz){
        this.clz=clz;
    }
    public T handle(ResultSet rs) throws SQLException {
        T t=null;
        while(rs.next()){
            try {
                 t=clz.newInstance();
                BeanInfo bi= Introspector.getBeanInfo(clz);
                PropertyDescriptor[] pds=bi.getPropertyDescriptors();
                for(PropertyDescriptor pd:pds){
                    String name=pd.getName();
                    Method mtd=pd.getWriteMethod();
                    try {
                        mtd.invoke(t,rs.getObject(name));
                    } catch (InvocationTargetException e) {
                        continue;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return t;
    }
}

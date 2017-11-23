package cn.tedu.utils;



import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tarena on 2016/9/5.
 */
public class MyBeanListhandler<T> implements MyResultSetHandler<List<T>> {
    private Class<T> clz;
    public MyBeanListhandler(Class<T> clz){
        this.clz=clz;
    }
    public List<T> handler(ResultSet rs) throws Exception {
        List<T> list=new ArrayList<T>();
        while(rs.next()){

                T t=clz.newInstance();
                BeanInfo bi= Introspector.getBeanInfo(clz);
                PropertyDescriptor[] pds=bi.getPropertyDescriptors();
                for(PropertyDescriptor pd:pds){
                    String name=pd.getName();
                    Method mtd=pd.getWriteMethod();
                    try {
                        mtd.invoke(t, rs.getObject(name));
                    }catch(SQLException e){
                        continue;
                    }
                }
            list.add(t);
        }
        return list.size()==0?null:list;
    }
}

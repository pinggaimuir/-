package cn.tedu.factory;

import java.util.Properties;

/**
 * Created by tarena on 2016/9/3.
 */
public class UserDaoFactory {
    private UserDaoFactory(){}
    private static UserDaoFactory factory=new UserDaoFactory();
    public static UserDaoFactory getFactory(){
        return factory;
    }
    public static Object getUserDao(){
        Properties prop=new Properties();
        return null;
    }
}

package cn.tedu.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 获取配置文件中的属性
 * Created by tarena on 2016/9/4.
 */
public class PropUtils {
    private PropUtils(){}
    private static Properties prop=null;
    static{
         prop=new Properties();
        try {
            String path=PropUtils.class.getClassLoader().getResource("config.properties").getPath();
            prop.load(new FileInputStream(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static Properties getProp(){
        return prop;
    }

    public static String getProp(String key){
        return prop.getProperty(key);
    }
}

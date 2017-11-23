package cn.tedu.factory;

import cn.tedu.utils.PropUtils;

/**
 * 通过反射获取Dao实现类的实例
 * Created by tarena on 2016/9/3.
 */
public class BasicFactory {

    private static BasicFactory factory=new BasicFactory();
    private BasicFactory(){}
    public static BasicFactory getFactory(){
        return factory;
    }

    /**
     * 通过类获取类的实例
     * @param clz 类
     * @return 类实例
     */
    public  <T>T getInstance(Class<T> clz){
        String daoImplStr= PropUtils.getProp(clz.getSimpleName());
        try {
            Class daoImplClz=Class.forName(daoImplStr);
            return (T)daoImplClz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}

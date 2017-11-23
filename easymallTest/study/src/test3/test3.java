package test3;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 获得泛型数组的真实类型
 * Created by tarena on 2016/9/13.
 */
public class test3 {
    public static void main(String[] args) {
        try {
            Method applyMethod=test3.class.getMethod("applyList",List.class);//掉用该泛型数组的方法知道到该泛型的类型
            Type[] types=applyMethod.getGenericParameterTypes();
            ParameterizedType pType=(ParameterizedType)types[0];//参数化类型
            System.out.println(pType.getRawType());
            System.out.println(pType.getActualTypeArguments());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }
    public void applyList(List<String> str){
        List<String> list=new ArrayList<String>();
    }
}

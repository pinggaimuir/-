package com.gao.practice9_23;

import java.util.List;

/**
 * ThreadLocal
 * Created by tarena on 2016/9/23.
 */
public class PriviThreadLocal {
    private static ThreadLocal<List<String>> threadLocal =new ThreadLocal();
    public static void setPriviList(List<String> Plist){
        threadLocal.set(Plist);
    }
    public static List<String> getPriviList(){
        return threadLocal.get();
    }
}

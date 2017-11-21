package queue.queuetest;

import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by gao on 2016/11/29.
 */
public class ConcurentHashMap {
    public static void main(String[] args) {
        ConcurrentHashMap<String,String> map=new ConcurrentHashMap<String, String>();
        System.out.println(map);
    }

    /**
     * 导航map的作用相当于根据制定条件返回对用的子map
     * headMap 小于的
     * tailMap  大于的
     * subMap 中间的
     */
    @Test
    public void testNavigableMap(){
        ConcurrentNavigableMap<String,String> map=new ConcurrentSkipListMap<String, String>();
        map.put("1","gaojan");
        map.put("2","ning");
        map.put("3","fengfeng");
    }
}

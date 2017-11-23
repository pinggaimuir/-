package com.gao.beantest1;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by tarena on 2016/9/20.
 */
@Component
public class Message {
    @Resource(name = "oneList")
    private List<String> oneList;
    @Resource(name="oneMap")
    private Map<String,Object> oneMap;
    @Resource(name="oneSet")
    private Set<Date> oneSet;
    @Resource(name="oneProp")
    private Properties oneProp;

    @Override
    public String toString() {
        return "Message{" +
                "oneList=" + oneList +
                ", oneMap=" + oneMap +
                ", oneSet=" + oneSet +
                ", oneProp=" + oneProp +
                '}';
    }
//    @PostConstruct
//    public void init(){
//        System.out.println("初始化了");
//    }
//    @PreDestroy
//    public void destroy(){
//        System.out.println("销毁了");
//    }
    public List<String> getOneList() {
        return oneList;
    }

    public Map<String, Object> getOneMap() {
        return oneMap;
    }

    public Properties getOneProp() {
        return oneProp;
    }

    public Set<Date> getOneSet() {
        return oneSet;
    }
}

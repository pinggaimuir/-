package com.gao.beantest1;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by tarena on 2016/9/20.
 */
@Component("userDao1")
public class UserDao extends  BaseDao {
    @Value("#{p.name}")
    private String name;
/*    @Autowired
    @Qualifier("bean2qqq")//指明id，只能通过Name匹配
    private Bean2 bean2;*/

    //结合上面两个注解  功能相同
//    @Resource(name = "bean234")
    @Resource(name="p")
    private Bean2 bean2;
    @Resource
    private Bean1 bean1;

    public Bean1 getBean1() {
        return bean1;
    }

    public Bean2 getBean2() {
        return bean2;
    }


    public String getName() {
        return name;

    }

    public void addUser(){
        dataSource.getCon();
    }
}

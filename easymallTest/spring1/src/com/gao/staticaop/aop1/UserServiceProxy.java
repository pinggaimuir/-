package com.gao.staticaop.aop1;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by tarena on 2016/9/21.
 */
@Component("userServiceProxy")
@Lazy
public class UserServiceProxy implements UserService {
    @Resource(name="userService")
    private UserServiceImpl usi;
    @Resource(name="tx")
    private txTransaction tx;
    public void add(){
        try{
            tx.startTransaction();
            usi.add();
            tx.commitTransaction();
        }catch(Exception e){
//            tx.rollbackTransaction();
        }
    }

    @Override
    public void del() {

    }
}

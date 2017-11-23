package com.gao.beantest1;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by tarena on 2016/9/20.
 */
@Service("propService")
public class PropService {
    @Resource(name="propDao")
    private PropDao propDao;
    public void printUrl(){
        System.out.println(propDao.getUrl());
    }
}

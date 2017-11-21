package com.seckill.mapper;

import com.seckill.pojo.Seckill;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by gao on 2016/11/20.
 */
@RunWith(SpringJUnit4ClassRunner.class )
@ContextConfiguration({"classpath:spring/applicationContext.xml","classpath:spring/applicationContext-mybatis.xml"})
public class SeckillMapperTest {
    @Resource
    private SeckillMapper seckillMapper;
    @org.junit.Test
    public void testReduceNumber() throws Exception {
        long id=1000;
        Seckill seckill=seckillMapper.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @org.junit.Test
    public void testQueryById() throws Exception {

    }

    @org.junit.Test
    public void testQueryAll() throws Exception {

    }
}
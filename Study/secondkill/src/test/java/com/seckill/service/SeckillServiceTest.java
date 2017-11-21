package com.seckill.service;

import com.seckill.common.Exposer;
import com.seckill.common.SeckillExecution;
import com.seckill.pojo.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by gao on 2016/11/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/applicationContext.xml",
                       "classpath:spring/applicationContext-mybatis.xml",
                        "classpath:spring/applicationContext-transaction.xml"})
public class SeckillServiceTest {
    private final Logger logger= LoggerFactory.getLogger(this.getClass());
    @Resource
    private SeckillService seckillService;
    @Test
    public void testGetSeckillList() throws Exception {
        List<Seckill> list= seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void testGetSeckill() throws Exception {

    }




}
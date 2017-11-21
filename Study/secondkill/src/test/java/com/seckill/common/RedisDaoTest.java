package com.seckill.common;

import junit.framework.TestCase;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static org.junit.Assert.*;

/**
 * Created by gao on 2016/11/23.
 */
public class RedisDaoTest extends TestCase{

    public void testRedis(){
        JedisPool jedisPool=new JedisPool("172.16.8.130",6379);
        Jedis jedis=jedisPool.getResource();
        try{
            jedis.set("gao","jian");
        }catch(Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        jedis.close();
    }

}
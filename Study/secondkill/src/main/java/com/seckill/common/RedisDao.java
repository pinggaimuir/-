package com.seckill.common;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.seckill.pojo.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 *  redis缓存访问对象
 * Created by gao on 2016/11/22.
 */
public class RedisDao {

    private final JedisPool jedisPool;

    public RedisDao(String ip,int port){
        jedisPool=new JedisPool(ip,port);
    }
    private Logger logger= LoggerFactory.getLogger(RedisDao.class);
    //序列化schema
    private RuntimeSchema<Seckill> schema=RuntimeSchema.createFrom(Seckill.class);

    /**
     * 根据key从redis缓存中获取seckill
     * @param seckillId
     * @return
     */
    public Seckill getSeckill(Long seckillId){
            try{
                Jedis jedis=jedisPool.getResource();
                try {
                    String key = "seckill:" + seckillId;
                    //从缓存中获取
                    byte[] bytes = jedis.get(key.getBytes());
                    if (bytes != null) {
                        Seckill seckill = schema.newMessage();
                        //反序列化
                        ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
                        return seckill;
                    }
                }finally {
                    jedis.close();
                }
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
        return null;
    }

    /**
     * 添加redis缓存
     * @param seckill
     * @return 成功返回OK，失败返回错误信息
     */
    public String setSeckill(Seckill seckill){
        try{
            Jedis jedis=jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                //反序列化
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                int timeout = 60 * 60;//一小时缓存时间
                String result = jedis.setex(key.getBytes(), timeout, bytes);
                return result;
            }finally{
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}

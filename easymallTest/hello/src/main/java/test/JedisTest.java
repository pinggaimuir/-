package test;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tarena on 2016/11/8.
 */
public class JedisTest {
    @Test
    public void testJedis(){
        Jedis jedis=new Jedis("172.16.8.173",6379);
        jedis.set("name","gao");
        String name=jedis.get("name");
        System.out.println(name);
        jedis.close();
    }
    @Test
    public void testShardJedis(){
        List<JedisShardInfo> infoList=new ArrayList();
        JedisShardInfo info1=new JedisShardInfo("172.16.8.173",6379);
        JedisShardInfo info2=new JedisShardInfo("172.16.8.157",6379);
        infoList.add(info1);
        infoList.add(info2);
        ShardedJedis sJedis=new ShardedJedis(infoList);
        for (int i = 0; i < 100; i++) {
            sJedis.set("name"+i,""+i);
        }
        sJedis.close();
    }
    @Test
    public void testPoolJedis(){
        Set<String> sentinels=new HashSet<>();
        sentinels.add("172.16.8.157:26381");
        sentinels.add("172.16.8.157:26382");
        JedisSentinelPool pool=new JedisSentinelPool("mymaster",sentinels);
        Jedis jedis=pool.getResource();
        jedis.set("name","jian");
        pool.returnResource(jedis);
    }
}

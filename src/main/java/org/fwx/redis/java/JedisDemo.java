package org.fwx.redis.java;

import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

/**
 * @ClassName Jedis
 * @Description TODO
 * @Author Fwx
 * @Date 2024/4/17 11:26
 * @Version 1.0
 */
public class JedisDemo {

    /**
     * 手动创建Redis连接
     */
    @Test
    public void redisTest(){
        Jedis jedis = new Jedis("192.168.2.100", 6379);

        Set<String> keys = jedis.keys("*");
        System.out.println("keys = " + keys);

        jedis.close();
    }

    /**
     * 使用Redis连接池
     */
    @Test
    public void redisPoolTest(){
        //连接池的配置信息
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(100);       //最多的连接个数
        jedisPoolConfig.setMaxIdle(10);         //最多空闲的连接个数
        jedisPoolConfig.setMinIdle(2);          //最小的空闲个数
        jedisPoolConfig.setTestOnBorrow(true);  //在获取连接对象时是否验证该连接对象的连通性

        //创建连接池对象
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "192.168.2.100", 6379);

        Jedis jedis = jedisPool.getResource();

        String s1 = jedis.get("s1");
        System.out.println("s1 = " + s1);

        jedis.close();
    }
}

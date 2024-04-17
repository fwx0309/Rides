package org.fwx.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

/**
 * @ClassName RedisTest
 * @Description TODO
 * @Author Fwx
 * @Date 2024/4/17 12:53
 * @Version 1.0
 */
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test(){
        Set keys = redisTemplate.keys("*");
        System.out.println("keys = " + keys);
    }
}

package org.fwx.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @ClassName RedisController
 * @Description TODO
 * @Author Fwx
 * @Date 2024/4/17 12:31
 * @Version 1.0
 */
@RestController
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/redis")
    public void redis(){
        Set keys = redisTemplate.keys("*");
        System.out.println("keys = " + keys);
    }
}

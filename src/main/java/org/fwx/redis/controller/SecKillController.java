package org.fwx.redis.controller;

import org.fwx.redis.service.SecKillRedisByScriptService;
import org.fwx.redis.service.SecKillRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

/**
 * @ClassName SecKillController
 * @Description TODO
 * @Author Fwx
 * @Date 2024/4/17 20:55
 * @Version 1.0
 */
@Controller
public class SecKillController {

    @Autowired
    private SecKillRedisService secKillRedis;

    @Autowired
    private SecKillRedisByScriptService secKillRedisByScriptService;

    @RequestMapping("/seckill")
    @ResponseBody
    public String secKill(String prodid){
//        System.out.println("prodid = " + prodid);

        String userid = new Random().nextInt(50000) +"" ;

//        secKillRedis.doSecKill(userid,"0101");

        secKillRedisByScriptService.doSecKill(userid,"0101");

        return "success";
    }
}

package org.fwx.redis.service;

import java.io.IOException;
import java.util.*;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class SecKillRedisByScriptService {

	@Autowired
	private RedisTemplate redisTemplate;

	// 秒杀脚本，用于处理用户抢购逻辑
	private String secKillScript ="local userid=KEYS[1];\r\n" +
			"local prodid=KEYS[2];\r\n" + 
			"local qtkey='sk:'..prodid..\":qt\";\r\n" + 
			"local usersKey='sk:'..prodid..\":user\";\r\n" +
			"local userExists=redis.call(\"sismember\",usersKey,userid);\r\n" + 
			"if tonumber(userExists)==1 then \r\n" + 
			"   return '2';\r\n" +
			"end\r\n" + 
			"local num= redis.call(\"get\" ,qtkey);\r\n" + 
			"if tonumber(num)<=0 then \r\n" + 
			"   return '0';\r\n" +
			"else \r\n" + 
			"   redis.call(\"decr\",qtkey);\r\n" + 
			"   redis.call(\"sadd\",usersKey,userid);\r\n" + 
			"end\r\n" + 
			"return '1'" ;

	private String secKillScript2 =
			"local userExists=redis.call(\"sismember\",\"{sk}:0101:usr\",userid);\r\n" +
			" return 1";

	/**
	 * 执行秒杀操作。
	 * @param uid 用户ID
	 * @param prodid 商品ID
	 * @return 返回执行结果，true表示执行成功
	 */
	public boolean doSecKill(String uid,String prodid) {

		// 准备脚本执行所需的KEYS参数
		// 根据脚本和返回值类型创建DefaultRedisScript对象，泛型定义为返回值类型
		DefaultRedisScript<String> redisScript = new DefaultRedisScript<>(secKillScript, String.class);
		// 执行秒杀脚本
		List<String> keys = Arrays.asList(uid,prodid);
		Object result = redisTemplate.execute(redisScript, keys);

//		System.out.println("result = " + result);

		String reString=String.valueOf(result);
		if ("0".equals( reString )  ) {
			System.err.println("已抢空！！");
		}else if("1".equals( reString )  )  {
			System.out.println("抢购成功！！！！");
		}else if("2".equals( reString )  )  {
			System.err.println("该用户已抢过！！");
		}else{
			System.err.println("抢购异常！！");
		}
		return true;
	}
}

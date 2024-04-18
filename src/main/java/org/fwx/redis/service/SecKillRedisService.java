package org.fwx.redis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 秒杀，存在库存遗留问题！！！
 */
@Service
public class SecKillRedisService {

	@Autowired
	private RedisTemplate redisTemplate;

	//秒杀过程
	public  boolean doSecKill(String uid,String prodid)  {
		//1 uid和prodid非空判断
		if(uid == null || prodid == null) {
			return false;
		}

		//2 拼接key
		// 2.1 库存key
		String kcKey = "sk:"+prodid+":qt";
		// 2.2 秒杀成功用户key
		String userKey = "sk:"+prodid+":user";

		//3 秒杀过程
		//使用事务
		ArrayList results = (ArrayList)redisTemplate.execute(new SessionCallback<List<Object>>() {
			public List<Object> execute(RedisOperations operations) throws DataAccessException {
				operations.watch(kcKey);
				//3.1 获取库存，如果库存null，秒杀还没有开始
				Object kc = operations.opsForValue().get(kcKey);
				if(kc == null) {
					System.out.println("秒杀还没有开始，请等待");
					return null;
				}

				// 3.2 判断用户是否重复秒杀操作
				if(operations.opsForSet().isMember(userKey, uid)) {
					System.out.println("已经秒杀成功了，不能重复秒杀");
					return null;
				}

				//3.3 判断如果商品数量，库存数量小于1，秒杀结束
				if ((Integer)kc <= 0) {
					System.out.println("秒杀已经结束了");
					return null;
				}

				//3.4 事务操作数据
				operations.multi();
				// 操作库存数
				operations.opsForValue().decrement(kcKey);
				// 添加用户信息
				operations.opsForSet().add(userKey, uid);
				return operations.exec();
			}
		});


		if(results != null && results.size() > 0 ) {
			System.out.println("秒杀成功了..");
			return true;
		} else {
			System.out.println("秒杀失败了....");
			return false;
		}
	}
}

















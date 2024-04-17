package org.fwx.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.*;

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
//    private RedisTemplate redisTemplate;
    private StringRedisTemplate redisTemplate;

    @Test
    public void test() {
        Set keys = redisTemplate.keys("*");
        System.out.println("keys = " + keys);
    }


    /**
     * 清库
     */
    @Test
    public void flushDBTest() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
        redisTemplate.getConnectionFactory().getConnection().close();
    }

    /**
     * key相关操作
     */
    @Test
    public void keyTest() {
        //获取所有的key
        Set<String> keys = redisTemplate.keys("*");
        System.out.println(keys);

        //判断指定的key是否存在
        Boolean k1 = redisTemplate.hasKey("k1");
        System.out.println("判断指定的key是否存在" + k1);

        //删除指定key
        Boolean k11 = redisTemplate.delete("k1");
        System.out.println("判断是否删除成功" + k11);

    }

    /**
     * string类型数据操作
     */
    @Test
    public void stringTest() {
        //操作字符串StringRedisTemplate会把对每一种数据的操作单独封装成一个类。
        //string操作
        ValueOperations<String, String> forValue = redisTemplate.opsForValue(); //valoperations专门操作字符

        //存值
        forValue.set("k1", "vq");

        //取值
        String k12 = forValue.get("k1");
        System.out.println(k12);

        //判断是否存在相同的key 如果没有则写入
        Boolean aBoolean = forValue.setIfAbsent("k999", "v3");
        System.out.println("是否存入成功" + aBoolean);

        //存储多个值
        Map<String, String> map = new HashMap<>();
        map.put("k2", "www");
        map.put("k3", "12");
        forValue.multiSet(map);

        //获取多个值
        List list = new ArrayList();
        list.add("k2");
        list.add("k3");
        List s = forValue.multiGet(list);
        System.out.println(s);

        //删除
        redisTemplate.delete("k1");

        //自增
        //incr
        forValue.set("k4", "12");
        Long k3 = forValue.increment("k4", 20);
        System.out.println("k3======" + k3);

        //自减
        //decr
        forValue.set("k5", "10");
        Long k5 = forValue.decrement("k5", 1);
        System.out.println(k5);
    }


    /**
     * list数据类型操作
     */
    @Test
    public void listTest() {
        ListOperations<String, String> forList = redisTemplate.opsForList();

        //增加
        forList.leftPushAll("k1", "ww", "dd", "ff", "sss", "ww");

        //获取
        List<String> k1 = forList.range("k1", 0, -1);
        System.out.println(k1);

        //移出并获取列表的元素
        System.out.println(forList.leftPop("k1"));
    }


    /**
     * set数据类型操作
     */
    @Test
    public void setTest() {
        //set操作
        SetOperations<String, String> forSet = redisTemplate.opsForSet();

        //增加
        forSet.add("k1", "www", "ff", "cc", "vv", "sssssdfaf");
        forSet.add("k2", "www", "ss", "ffff", "qqq");

        //取值
        System.out.println(forSet.members("k1"));

        //随机获取一个值
        System.out.println(forSet.distinctRandomMembers("k1", 1));

        //读取两个集合的交集
        System.out.println(forSet.intersect("k1", "k2"));
    }


    /**
     * hash数据类型操作
     */
    @Test
    public void hasdTest() {
        HashOperations<String, Object, Object> forHash = redisTemplate.opsForHash();

        //增加
        forHash.put("k6", "name", "www");
        forHash.put("k6", "age", "16");

        //获取key
        Set<Object> k13 = forHash.keys("k6");
        System.out.println(k13);

        Map<String, String> map = new HashMap<>();
        map.put("name", "mmm");
        map.put("age", "16");
        forHash.putAll("k7", map);

        //获取K6的所有数据
        Map<Object, Object> k1 = forHash.entries("k6");
        System.out.println("entries" + k1);

        //获取key
        Set<Object> k7 = forHash.keys("k7");
        System.out.println(k7);

        //获取值
        List<Object> k71 = forHash.values("k7");
        System.out.println(k71);

        //删除
        redisTemplate.delete("k6");

        // 获取存储在哈希表中指定字段的值
        Object o = forHash.get("k7", "name");
        System.out.println(o);
    }


    /**
     * zset数据类型操作
     */
    @Test
    public void zsetTest() {
        ZSetOperations<String, String> forZSet = redisTemplate.opsForZSet();

        HashSet<ZSetOperations.TypedTuple<String>> set = new HashSet<>();
        set.add(new DefaultTypedTuple("java",1d));
        set.add(new DefaultTypedTuple("python",2d));

        //增加
        forZSet.add("k1", "mach", 22);
        forZSet.add("k2",set);

        //取值
        Set<String> k1 = forZSet.range("k1", 0, -1);
        System.out.println(k1);
    }
}

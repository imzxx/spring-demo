package com.example.redis;


import com.example.redis.demo.RedisApplication;
import com.example.redis.demo.util.RedisUtils;
import com.sun.glass.ui.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import sun.plugin.security.JDK11ClassFileTransformer;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;


@SpringBootTest(classes = RedisApplication.class)
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    private Redisson redisson;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void test01() {
        redisTemplate.opsForValue().set("k1", "v1");
        System.out.println(redisTemplate.opsForValue().get("k1"));

    }

    @Test
    public void test02() throws InterruptedException {

        //System.out.println(redisUtils.get("k1"));
        RLock lock = redisson.getLock("test-lock");
        lock.lock(100, TimeUnit.SECONDS);
        redisUtils.hashPutAll("customer", "name", "张三");
        Object value = redisUtils.hashGet("customer", "id");
        TimeUnit.SECONDS.sleep(100);
        if (lock.isLocked() && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
        System.out.println(redisUtils.hashGet("customer", "name"));
        System.out.println(redisUtils.hashGet("customer", "name").equals("张三"));
        System.out.println("测试提交冲突");
    }

}

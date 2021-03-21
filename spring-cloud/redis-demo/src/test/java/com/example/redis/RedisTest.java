package com.example.redis;



import com.example.redis.demo.RedisApplication;
import com.example.redis.demo.util.RedisUtils;
import com.sun.glass.ui.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import sun.plugin.security.JDK11ClassFileTransformer;

import javax.annotation.Resource;


@SpringBootTest(classes = RedisApplication.class)
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void test01(){
        redisTemplate.opsForValue().set("k1","v1");
        System.out.println(redisTemplate.opsForValue().get("k1"));

    }

    @Test
    public void test02(){

        System.out.println(redisUtils.get("k1"));

        //redisUtils.hashPutAll("customer", "name", "张三");
        Object value=redisUtils.hashGet("customer", "id");
        System.out.println(value);
        System.out.println(redisUtils.hashGet("customer","name").equals("张三"));
    }

}

package com.example.redis.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @ClassName RedisLockUtil
 * @Description Redis分布式锁实现
 * @Author zxx
 * @Date 2021/3/30 11:05
 * @Version 1.0
 **/
@Component
public class RedisLockUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //默认超时时间
    private static final Integer LOCK_TIME_OUT = 1000;

    /**
     * 获取锁接口
     *
     * @param key     锁名称
     * @param timeout 超时时间
     * @return true:获取成功 false:获取失败
     */
    public Boolean tryLock(String key, Long timeout) {
        //获取当前系统的开始时间
        Long startTime = System.currentTimeMillis();

        //默认返回值 false
        boolean result = false;

        //循环获取锁，获取锁成功推出 获取锁超时退出
        while (true) {
            //判断是否超时
            if (System.currentTimeMillis() - startTime >= timeout) {
                break;
            } else {
                //获取锁
                result = lock(key);
                if (result) {
                    break;
                } else {
                    //获取锁失败，线程休眠100毫秒继续获取锁
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
        return result;
    }

    /**
     * 获取锁
     *
     * @param key
     * @return
     */
    private Boolean lock(String key) {
        return (Boolean) stringRedisTemplate.execute((RedisCallback) redisConnection -> {
            //获取当前系统的时间
            Long time = System.currentTimeMillis();
            //设置锁超时时间
            Long timeout = time + LOCK_TIME_OUT + 1;

            //setnx加锁并获取锁的结果
            Boolean result = redisConnection.setNX(key.getBytes(), String.valueOf(timeout).getBytes());
            if (result) {
                return true;
            }

            //加锁失败判断锁是否超时
            if (checkLock(key, timeout)) {
                byte[] newTime = redisConnection.getSet(key.getBytes(), String.valueOf(timeout).getBytes());
                if (time > Long.valueOf(new String(newTime))) {
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * 判断锁是否超时
     *
     * @param key
     * @param tiemout
     * @return
     */
    private Boolean checkLock(String key, Long tiemout) {
        return (Boolean) stringRedisTemplate.execute((RedisCallback) redisConnection -> {
            //获取锁的超时时间
            byte[] bytes = redisConnection.get(key.getBytes());
            try {
                //判断锁的有效期是否大于当前时间
                if (tiemout > Long.valueOf(new String(bytes))) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return false;
        });
    }

}

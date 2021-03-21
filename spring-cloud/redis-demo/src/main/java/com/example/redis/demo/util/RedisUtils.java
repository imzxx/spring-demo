package com.example.redis.demo.util;/**
 * @ClassName RedisUtils
 * @Description TODO
 * @Author zxx
 * @Date 2021/3/20 21:55
 * @Version 1.0
 **/

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import sun.security.x509.DeltaCRLIndicatorExtension;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisUtils
 * @Description redis工具类
 * @Author zxx
 * @Date 2021/3/20 21:55
 * @Version 1.0
 **/
@Component
@Slf4j
public class RedisUtils {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 返回redis模板
     *
     * @return
     */
    public RedisTemplate<String, Object> redisTemplate() {
        return redisTemplate;
    }

    /**
     * 设置缓存失效时间
     *
     * @param key
     * @param time
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}缓存失效时间设置失败", key);
            return false;
        }
    }

    /**
     * 根据key，获取过期时间
     *
     * @param key
     * @return 时间（秒）返回0代表为永久有效
     */
    public Long getExpire(String key) {
        if (key == null) {
            return null;
        }
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 删除缓存
     *
     * @param keys
     * @return 返回为null时，缓存删除失败
     */
    public Long delete(String... keys) {
        try {
            return redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(keys));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}缓存删除失败", keys);
            return null;
        }
    }

    /**
     * 删除缓存
     *
     * @param key
     * @return 返回为null时，缓存删除失败
     */
    public Boolean delete(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}缓存删除失败", key);
            return null;
        }
    }

    /**
     * 获取缓存
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        if (key == null) {
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 插入缓存
     *
     * @param key   键
     * @param value 值
     * @return 返回为null时，缓存设置失败
     */
    public Boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}缓存设置失败", key);
            return null;
        }
    }

    /**
     * 插入缓存并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间
     * @return 返回为null时，缓存设置失败
     */
    public Boolean set(String key, Object value, Long time) {
        try {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}缓存设置失败", key);
            return null;
        }
    }

    /**
     * 数字值递增
     *
     * @param key   键
     * @param delta 递增值
     * @return 返回为null时，设置失败
     */
    public Long increment(String key, long delta) {
        try {
            if (delta < 0) {
                return null;
            }
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}递增设置失败", key);
            return null;
        }
    }

    /**
     * 数字值递增
     *
     * @param key   键
     * @param delta 递增值
     * @return 返回为null时，设置失败
     */
    public Double increment(String key, double delta) {
        try {
            if (delta < 0) {
                return null;
            }
            return redisTemplate.opsForValue().increment(key, delta);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}递增设置失败", key);
            return null;
        }
    }

    /**
     * 数字值递减
     *
     * @param key   键
     * @param delta 递减值
     * @return 返回为null时，设置失败
     */
    public Long decrement(String key, long delta) {
        try {
            if (delta < 0) {
                return null;
            }
            return redisTemplate.opsForValue().decrement(key, delta);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}递减设置失败", key);
            return null;
        }
    }

    /**
     * Hash Get
     *
     * @param key     键
     * @param hashKey 项
     * @return
     */
    public Object hashGet(String key, String hashKey) {
        if (key == null || hashKey == null) {
            return null;
        }
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取hash键对应的所有键值对
     *
     * @param key 键
     * @return
     */
    public Map<Object, Object> hashEntries(String key) {
        if (key == null) {
            return null;
        }
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * hash设置键值对
     *
     * @param key 键
     * @param map 值
     * @return
     */
    public Boolean hashPutAll(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}设置失败", key);
            return false;
        }
    }

    /**
     * hash设置键值对
     *
     * @param key     键
     * @param hashKey 项
     * @param value   值
     * @return
     */
    public Boolean hashPutAll(String key, String hashKey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}设置失败", key);
            return false;
        }
    }

    /**
     * hash设置键值对，并设置失效时间
     *
     * @param key  键
     * @param map  值
     * @param time 时间
     * @return
     */
    public Boolean hashPutAll(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}设置失败", key);
            return false;
        }
    }

    /**
     * hash设置键值对
     *
     * @param key     键
     * @param hashKey 项
     * @param value   值
     * @param time    时间
     * @return
     */
    public Boolean hashPutAll(String key, String hashKey, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}设置失败", key);
            return false;
        }
    }

    /**
     * 删除hash
     *
     * @param key      键
     * @param hashKeys 项
     * @return 返回为null时，删除失败
     */
    public Long hashDelete(String key, Object... hashKeys) {
        try {
            if (key == null || hashKeys == null) {
                return null;
            }
            return redisTemplate.opsForHash().delete(key, hashKeys);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}删除失败", key);
            return null;
        }
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key     键
     * @param hashKey 项
     * @return
     */
    public Boolean hashHasKey(String key, Object hashKey) {
        if (key == null && hashKey == null) {
            return null;
        }
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * hash递增，如果不存在，则会创建一个新值，并将新值返回
     *
     * @param key     键
     * @param hashKey 项
     * @param delta   递增值
     * @return 返回为null时，设置失败
     */
    public Long hashIncrement(String key, String hashKey, long delta) {
        try {
            return redisTemplate.opsForHash().increment(key, hashKey, delta);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}设置失败", key);
            return null;
        }

    }

    /**
     * hash递增，如果不存在，则会创建一个新值，并将新值返回
     *
     * @param key     键
     * @param hashKey 项
     * @param delta   递增值
     * @return 返回为null时，操作失败
     */
    public Double hashIncrement(String key, String hashKey, double delta) {
        try {
            return redisTemplate.opsForHash().increment(key, hashKey, delta);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}设置失败", key);
            return null;
        }

    }

    /**
     * 获取集合中的所有值
     *
     * @param key 键
     * @return 返回null是，操作失败
     */
    public Set<Object> setMembers(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}查询失败", key);
            return null;
        }
    }

    /**
     * 查询集合中是否存在value值
     *
     * @param key   键
     * @param value 值
     * @return 返回null时，操作失败
     */
    public Boolean isMember(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}查询失败", key);
            return null;
        }
    }

    /**
     * 将数据插入至集合中
     *
     * @param key    键
     * @param values 值
     * @return 返回null时，操作失败
     */
    public Long setAdd(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}添加失败", key);
            return null;
        }
    }

    /**
     * 获取集合值的长度
     *
     * @param key
     * @return 返回null时，操作失败
     */
    public Long setSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}查询失败", key);
            return null;
        }
    }

    /**
     * 移除集合中的值
     *
     * @param key    键
     * @param values 值
     * @return 返回null时，操作失败
     */
    public Long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}移除失败", key);
            return null;
        }
    }

    /**
     * 查询列表中的值
     *
     * @param key   键
     * @param start 开始
     * @param end   结束
     * @return 返回null时，操作失败
     */
    public List<Object> listGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}查询失败", key);
            return null;
        }
    }
}

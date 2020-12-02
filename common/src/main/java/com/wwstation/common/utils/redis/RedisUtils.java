package com.wwstation.common.utils.redis;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * redis 操作类
 *
 * @author william
 * @description
 * @Date: 2020-12-02 15:56
 */
public class RedisUtils {
    @Autowired
    private static RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private static ValueOperations<String, String> valueOperations;
    @Autowired
    private static HashOperations<String, String, Object> hashOperations;
    @Autowired
    private static SetOperations<String, Object> setOperations;

    public static boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 存值并设定过期时间
     *
     * @param key
     * @param value
     * @param expire
     */
    public static void set(String key, Object value, long expire) {
        if (expire != RedisExpireTime.NEVER_EXPIRE.getExpireTime()) {
            valueOperations.set(key, toJson(value), expire);
        } else {
            set(key, toJson(value));
        }
    }

    /**
     * 直接存值
     *
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        valueOperations.set(key, toJson(value));
    }

    /**
     * 取值并为该值续命
     *
     * @param key
     * @param clazz  取值要转成的类型
     * @param expire 续命时间：毫秒
     * @param <T>
     * @return
     */
    public static <T> T getAndExpire(String key, Class<T> clazz, long expire) {
        String value = valueOperations.get(key);
        if (StringUtils.isNotEmpty(value) && expire != RedisExpireTime.NEVER_EXPIRE.getExpireTime()) {
            redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS);
        }
        return value == null ? null : fromJson(value, clazz);
    }

    /**
     * 直接取值并尝试转成指定格式
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T get(String key, Class<T> clazz) {
        String json = valueOperations.get(key);
        return fromJson(json, clazz);
    }

    /**
     * 直接取值并续命
     *
     * @param key
     * @param expire
     * @return
     */
    public static String getAndExpire(String key, long expire) {
        String value = valueOperations.get(key);
        if (StringUtils.isNotEmpty(value) && expire != RedisExpireTime.NEVER_EXPIRE.getExpireTime()) {
            redisTemplate.expire(key, expire, TimeUnit.MILLISECONDS);
        }
        return value;
    }

    public static void delete(String key) {
        redisTemplate.delete(key);
    }

    public static void deleteBatch(Collection<String> keys) {
        redisTemplate.delete(keys);
    }


    public static void putMap(String mapName, Map<String, Object> map) {
        hashOperations.putAll(mapName, map);
    }

    public static void putMap(String mapName, String key, Object value) {
        hashOperations.put(mapName, key, value);
    }

    public static void putSet(String setName,Object value){
        setOperations.add(setName,value);
    }


    /**
     * Object转成JSON数据
     */
    private static String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }
        return JSONObject.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private static <T> T fromJson(String json, Class<T> clazz) {
        T targetType;
        try {
            targetType = JSONObject.parseObject(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return targetType;
    }

}

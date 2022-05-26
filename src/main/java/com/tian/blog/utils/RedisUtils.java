package com.tian.blog.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

public class RedisUtils {
    public static <T> void set(StringRedisTemplate redisTemplate, String key, T data) {
        String userJson = JSON.toJSONString(data);
        redisTemplate.opsForValue().set(key, userJson);
    }

    public static <T> void set(StringRedisTemplate redisTemplate, String key, T data, long expire) {
        String userJson = JSON.toJSONString(data);
        redisTemplate.opsForValue().set(key, userJson, expire, TimeUnit.MILLISECONDS);
    }

    public static String readString(StringRedisTemplate redisTemplate, String key){
        return redisTemplate.opsForValue().get(key);
    }

    public static boolean delete(StringRedisTemplate redisTemplate, String key){
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }
}

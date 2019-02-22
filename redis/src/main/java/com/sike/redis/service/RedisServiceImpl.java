package com.sike.redis.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.sike.service.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
@Service(version = "1.0.0")
public class RedisServiceImpl implements RedisService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void remove(String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    @Override
    public void removePattern(String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    public void remove(String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Serializable get(String key) {
        Serializable result = null;
        ValueOperations<Serializable, Serializable> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }

    @Override
    public boolean set(String key, Serializable value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Serializable> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean set(String key, Serializable value, Long expireTime) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Serializable> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void expireKey(String key, Long expireTime) {
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
    }
}

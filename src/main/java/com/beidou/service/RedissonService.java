package com.beidou.service;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * redisson业务层
 *
 * @author ginger
 * @create 2019-01-22 16:48
 */
@Service
public class RedissonService {

    @Autowired
    private RedissonClient redisson;


    public String get(String key) {
        Objects.requireNonNull(key);
        return (String) redisson.getBucket(key).get();
    }

    public void setEx(String key, String value, Integer ttl, TimeUnit timeUnit) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        if (null == ttl) {
            redisson.getBucket(key).set(value);
        } else {
            redisson.getBucket(key).set(value, ttl, timeUnit);
        }
    }

}

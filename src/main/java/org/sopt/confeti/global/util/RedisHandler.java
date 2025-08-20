package org.sopt.confeti.global.util;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.annotation.Handler;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Handler
@RequiredArgsConstructor
public class RedisHandler {

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, int ttl, TimeUnit ttlUnit) {
        redisTemplate.opsForValue().set(key, value, ttl, ttlUnit);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Object multiGet(Set<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}

package org.sopt.confeti.global.common.redis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.annotation.Handler;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

@Slf4j
@Handler
@RequiredArgsConstructor
public class RedisHandler {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedisSerializer serializer;

    @Builder
    @RequiredArgsConstructor
    public static class RedisData<T> {
        private final String key;
        private final T value;
    }

    public <T> void set(T value, RedisKey redisKey, Object... args) {
        String key = redisKey.getKey(args);

        if (key == null) {
            log.warn("RedisHandler.set : Redis key format error. Redis key : {}, args : {}", redisKey, args);
            return;
        }

        redisTemplate.opsForValue().set(key, serializer.serialize(value), redisKey.getTtl());
    }

    public <T> void multiSet(RedisKey redisKey, List<RedisData<T>> dataList) {
        for (RedisData<T> data : dataList) {
            redisTemplate.opsForValue().set(data.key, serializer.serialize(data.value), redisKey.getTtl());
        }
    }

    public <T> Optional<T> get(RedisKey redisKey, Object... args) {
        String key = redisKey.getKey(args);

        if (key == null) {
            log.warn("RedisHandler.get : Redis key format error. Redis key : {}, args : {}", redisKey, args);
            return Optional.empty();
        }

        T result = serializer.deserialize(
                redisTemplate.opsForValue().get(key),
                redisKey.getType()
        );

        return Optional.of(result);
    }

    public <T> List<T> getList(RedisKey redisKey, Object... args) {
        String key = redisKey.getKey(args);

        if (key == null) {
            log.warn("RedisHandler.getList : Redis key format error. Redis key : {}, args : {}", redisKey, args);
            return Collections.emptyList();
        }

        return serializer.deserializeToList(
                redisTemplate.opsForValue().get(key),
                redisKey.getType()
        );
    }

    public <T> Set<T> getSet(RedisKey redisKey, Object... args) {
        String key = redisKey.getKey(args);

        if (key == null) {
            log.warn("RedisHandler.getSet : Redis key format error. Redis key : {}, args : {}", redisKey, args);
            return Collections.emptySet();
        }

        return serializer.deserializeToSet(
                redisTemplate.opsForValue().get(key),
                redisKey.getType()
        );
    }

    public <T> List<T> multiGet(RedisKey redisKey, Set<String> keys) {
        List<T> results = new ArrayList<>(keys.size());

        for (String key : keys) {
            results.add(serializer.deserialize(
                    redisTemplate.opsForValue().get(key),
                    redisKey.getType()
            ));
        }

        return results;
    }

    public void delete(RedisKey redisKey, Object... args) {
        String key = redisKey.getKey(args);

        if (key == null) {
            log.warn("RedisHandler.delete : Redis key format error. Redis key : {}, args : {}", redisKey, args);
            return;
        }

        redisTemplate.delete(key);
    }
}

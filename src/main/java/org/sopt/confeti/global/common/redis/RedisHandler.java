package org.sopt.confeti.global.common.redis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.global.annotation.Handler;
import org.sopt.confeti.global.common.redis.RedisKey.KeyInfo;
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
        private final KeyInfo<T> keyInfo;
        private final T value;
    }

    public <T> void set(KeyInfo<T> keyInfo, T value) {
        if (isNotValidKeyInfo(keyInfo)) {
            log.warn("RedisHandler.set : Redis key format error. value : {}", value);
            return;
        }

        Optional<String> result = serializer.serialize(value);
        result.ifPresent(serialized ->
                redisTemplate.opsForValue().set(keyInfo.getKey(), serialized, keyInfo.getTtl()));
    }

    public <T> void multiSet(List<RedisData<T>> dataList) {
        Map<String, String> dataMap = new HashMap<>(dataList.size());

        for (RedisData<T> data : dataList) {
            KeyInfo keyInfo = data.keyInfo;

            Optional<String> result = serializer.serialize(data.value);
            result.ifPresent(serialized -> dataMap.put(keyInfo.getKey(), serialized));
        }

        redisTemplate.opsForValue().multiSet(dataMap);
    }

    public <T> Optional<T> get(KeyInfo<T> keyInfo) {
        if (isNotValidKeyInfo(keyInfo)) {
            log.warn("RedisHandler.get : Redis key format error.");
            return Optional.empty();
        }

        String cachedValue = redisTemplate.opsForValue().get(keyInfo.getKey());
        if (cachedValue == null) {
            return Optional.empty();
        }

        return serializer.deserialize(cachedValue, keyInfo.getType());
    }

    public <T> List<T> getList(KeyInfo<T> keyInfo) {
        if (isNotValidKeyInfo(keyInfo)) {
            log.warn("RedisHandler.getList : Redis key format error.");
            return Collections.emptyList();
        }

        String cachedValue = redisTemplate.opsForValue().get(keyInfo.getKey());
        if (cachedValue == null) {
            return List.of();
        }

        return serializer.deserializeToList(cachedValue, keyInfo.getType());
    }

    public <T> Set<T> getSet(KeyInfo<T> keyInfo) {
        if (isNotValidKeyInfo(keyInfo)) {
            log.warn("RedisHandler.getSet : Redis key format error.");
            return Collections.emptySet();
        }

        String cachedValue = redisTemplate.opsForValue().get(keyInfo.getKey());
        if (cachedValue == null) {
            return Set.of();
        }

        return serializer.deserializeToSet(cachedValue, keyInfo.getType());
    }

    public <T> List<T> multiGet(List<KeyInfo<T>> keyInfos) {
        if (keyInfos.isEmpty()) {
            return List.of();
        }

        List<String> keys = keyInfos.stream()
                .map(KeyInfo::getKey)
                .toList();
        Class<T> type = keyInfos.stream().findFirst().get().getType();

        List<String> cachedValues = redisTemplate.opsForValue().multiGet(keys);

        if (cachedValues == null) {
            return List.of();
        }

        List<T> results = new ArrayList<>(cachedValues.size());

        for (String cachedValue : cachedValues) {
            if (cachedValue == null) {
                continue;
            }

            Optional<T> result = serializer.deserialize(cachedValue, type);
            result.ifPresent(results::add);
        }

        return results;
    }

    public <T> void delete(KeyInfo<T> keyInfo) {
        if (isNotValidKeyInfo(keyInfo)) {
            log.warn("RedisHandler.delete : Redis key format error.");
            return;
        }

        redisTemplate.delete(keyInfo.getKey());
    }

    public <T> boolean hasKey(KeyInfo<T> keyInfo) {
        if (isNotValidKeyInfo(keyInfo)) {
            log.warn("RedisHandler.hasKey : Redis key format error.");
            return false;
        }

        return redisTemplate.hasKey(keyInfo.getKey());
    }

    private <T> boolean isNotValidKeyInfo(KeyInfo<T> keyInfo) {
        return keyInfo == null || !keyInfo.isValid();
    }
}

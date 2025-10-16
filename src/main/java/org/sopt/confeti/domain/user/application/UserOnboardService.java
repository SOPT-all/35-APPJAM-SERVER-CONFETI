package org.sopt.confeti.domain.user.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardCacheDTO;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserOnboardService {

    private static final String REDIS_KEY_PREFIX = "user:onboard:top-artists:%d";
    private static final int REDIS_TTL_DAY = 1;

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static String generateRedisKey(long userId) {
        log.info("Redis Key: {}", String.format(REDIS_KEY_PREFIX, userId));
        return String.format(REDIS_KEY_PREFIX, userId);
    }

    public UserOnboardCacheDTO getCachedArtists(long userId) {
        String key = generateRedisKey(userId);

        Object raw = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(raw)) {
            log.info("Get Top Artists from Redis Failed");
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        return objectMapper.convertValue(raw, UserOnboardCacheDTO.class);
    }

    public Set<String> getCachedExposedArtistIds(long userId) {
        try {
            return getCachedArtists(userId).exposedArtistIds();
        } catch (NotFoundException e) {
            return Collections.emptySet();
        }
    }

    public void cacheTopArtists(long userId, UserOnboardCacheDTO artistsDTO) {
        redisTemplate.opsForValue().set("TEST:" + userId, "test", REDIS_TTL_DAY, TimeUnit.DAYS);
        redisTemplate.opsForValue()
            .set(generateRedisKey(userId), artistsDTO, REDIS_TTL_DAY, TimeUnit.DAYS);
    }

    public void flushCachedTopArtists(long userId) {
        redisTemplate.delete(generateRedisKey(userId));
    }
}

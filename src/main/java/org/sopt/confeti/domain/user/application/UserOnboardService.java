package org.sopt.confeti.domain.user.application;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.user.application.dto.request.UserOnboardCacheTopArtistsDTO;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserOnboardService {

    private static final String REDIS_KEY_PREFIX = "user:onboard:top-artists:%d";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static String generateRedisKey(long userId) {
        return String.format(REDIS_KEY_PREFIX, userId);
    }

    public Set<String> getCachedTopArtists(long userId) {
        String key = generateRedisKey(userId);

        Object raw = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(raw)) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        return objectMapper.convertValue(raw, new TypeReference<>() {
        });
    }

    public void cacheTopArtists(long userId, UserOnboardCacheTopArtistsDTO artistsDTO) {
        redisTemplate.opsForValue().set(generateRedisKey(userId), artistsDTO.artistIds());
    }
}

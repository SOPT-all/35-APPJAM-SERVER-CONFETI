package org.sopt.confeti.domain.user.application;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.user.application.dto.request.UserOnboardCacheTopArtistsDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserOnboardService {

    private static final String REDIS_KEY_PREFIX = "user:onboard:top-artists:%d";

    private final RedisTemplate<String, Object> redisTemplate;

    private static String generateRedisKey(long userId) {
        return String.format(REDIS_KEY_PREFIX, userId);
    }

    public void cacheTopArtists(long userId, UserOnboardCacheTopArtistsDTO artistsDTO) {
        Set<String> artistIds = new HashSet<>(artistsDTO.artistIds());

        redisTemplate.opsForValue().set(generateRedisKey(userId), artistIds);
    }
}

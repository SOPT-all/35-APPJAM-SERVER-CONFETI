package org.sopt.confeti.domain.user.application;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.user.facade.dto.response.onboard.UserOnboardCacheDTO;
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.exception.NotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserOnboardService {

    private final RedisHandler redisHandler;

    public UserOnboardCacheDTO getCachedOnboardArtists(long userId) {
        Optional<UserOnboardCacheDTO> cachedTopArtists = redisHandler.get(RedisKey.USER_ONBOARD_TOP_ARTISTS.createKeyInfo(userId));
        return cachedTopArtists.orElse(UserOnboardCacheDTO.empty());
    }

    public void cacheOnboardArtists(long userId, UserOnboardCacheDTO artistsDTO) {
        redisHandler.set(RedisKey.USER_ONBOARD_TOP_ARTISTS.createKeyInfo(userId), artistsDTO);
    }

    public void flushCachedOnboardArtists(long userId) {
        redisHandler.delete(RedisKey.USER_ONBOARD_TOP_ARTISTS.createKeyInfo(userId));
    }

    public Set<String> getCachedExposedArtistIds(long userId) {
        try {
            return getCachedOnboardArtists(userId).exposedArtistIds();
        } catch (NotFoundException e) {
            return Collections.emptySet();
        }
    }

    public static void argParam(Object f, Object s, Object t) {
        // nothing
    }

    public static void vararg(Object... args) {
        // nothing
    }

    public static void main(String[] args) {
        String firstArg = "test";
        Long secondArg = 3L;
        Integer thirdArg = 4;

        long startArgParam = System.currentTimeMillis();

        for (int i = 0; i < 1000000; ++i) {
            argParam(firstArg, secondArg, thirdArg);
        }

        long endArgParam = System.currentTimeMillis();
        System.out.println("명시적 매개변수 실행시간 : " + (endArgParam - startArgParam) + "ms");

        long startVararg = System.currentTimeMillis();

        for (int i = 0; i < 1000000; ++i) {
            vararg(firstArg, secondArg, thirdArg);
        }

        long endVararg = System.currentTimeMillis();
        System.out.println("가변인자 실행시간 : " + (endVararg - startVararg) + "ms");
    }
}


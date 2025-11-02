package org.sopt.confeti.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.auth.jwt.JwtTokenExtractor;
import org.sopt.confeti.auth.jwt.JwtTokenGenerator;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.common.redis.RedisKey;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.common.redis.RedisHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReissueService {

    private final JwtTokenGenerator jwtTokenGenerator;
    private final JwtTokenExtractor jwtTokenExtractor;
    private final UserService userService;
    private final RedisHandler redisHandler;

    @Transactional
    public Token reissue(String refreshToken) {
        String userId = jwtTokenExtractor.getSubject(refreshToken);
        validateCachedRefreshToken(Long.parseLong(userId), refreshToken);

        Role role = userService.getRole(Long.parseLong(userId));
        OAuthProvider provider = jwtTokenExtractor.getProvider(refreshToken);

        String newAccessToken = jwtTokenGenerator.createAccessToken(userId, role, provider);
        String newRefreshToken = jwtTokenGenerator.createRefreshToken(userId, role, provider);

        cacheRefreshToken(Long.parseLong(userId), newRefreshToken);

        return new Token(newAccessToken, newRefreshToken);
    }

    private void validateCachedRefreshToken(long userId, String refreshToken) {
        String cachedRefreshToken = redisHandler.<String>get(RedisKey.USER_REFRESH_TOKEN.createKeyInfo(userId))
                .orElseThrow(() -> new ConfetiException(ErrorMessage.BAD_REQUEST));

        if (!cachedRefreshToken.equals(refreshToken)) {
            throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED);
        }
    }

    public void cacheRefreshToken(Long userId, String refreshToken) {
        redisHandler.set(RedisKey.USER_REFRESH_TOKEN.createKeyInfo(userId), refreshToken);
    }
}

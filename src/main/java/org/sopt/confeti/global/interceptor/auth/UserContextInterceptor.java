package org.sopt.confeti.global.interceptor.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.sopt.confeti.auth.jwt.JwtTokenExtractor;
import org.sopt.confeti.auth.jwt.TokenParser;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.domain.user.UserInfo;
import org.sopt.confeti.domain.user.infra.repository.UserRepository;
import org.sopt.confeti.global.annotation.Interceptor;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.interceptor.CustomInterceptor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Interceptor
@RequiredArgsConstructor
public class UserContextInterceptor implements HandlerInterceptor, CustomInterceptor {

    private final JwtTokenExtractor jwtTokenExtractor;
    private final TokenParser tokenParser;
    private final UserRepository userRepository;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull Object handler) {
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null) {
            return true;
        }

        Optional<Long> userId = getUserId(request);
        userId.ifPresent(this::setUserInfoToContext);

        return true;
    }

    @Override
    public int order() {
        return 1;
    }

    private void setUserInfoToContext(long userId) {
        UserInfo userInfo = userRepository.findById(userId)
            .map(User::toUserInfo)
            .orElseThrow(UnauthorizedException::wrong);
        UserContext.set(userInfo);
    }

    private Optional<Long> getUserId(HttpServletRequest request) {
        try {
            return getAccessToken(request)
                .map(jwtTokenExtractor::getSubject)
                .map(Long::valueOf);
        } catch (ExpiredJwtException e) {
            throw UnauthorizedException.expired();
        } catch (JwtException | IllegalArgumentException e) {
            throw UnauthorizedException.wrong();
        }
    }

    private Optional<String> getAccessToken(HttpServletRequest request)
        throws JwtException, IllegalArgumentException {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            return Optional.empty();
        }

        String token = tokenParser.getToken(authorization);
        if (!jwtTokenExtractor.isAccessToken(token)) {
            return Optional.empty();
        }

        return Optional.of(token);
    }
}

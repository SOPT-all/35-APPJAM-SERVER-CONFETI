package org.sopt.confeti.global.resolver.user;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.auth.jwt.JwtTokenExtractor;
import org.sopt.confeti.auth.jwt.TokenParser;
import org.sopt.confeti.global.annotation.UserId;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenExtractor jwtTokenExtractor;
    private final TokenParser tokenParser;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Long.class)
                && parameter.hasParameterAnnotation(UserId.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        UserId userIdAnnotation = parameter.getParameterAnnotation(UserId.class);
        if (Objects.isNull(userIdAnnotation)) {
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(token)) {
            if (userIdAnnotation.require()) {
                throw new ConfetiException(ErrorMessage.BAD_REQUEST);
            }

            return null;
        }

        String userId;
        try {
            userId = jwtTokenExtractor.getSubject(tokenParser.getToken(token));
        } catch (ExpiredJwtException e) {
            throw UnauthorizedException.expired();
        } catch (JwtException | IllegalArgumentException e) {
            throw UnauthorizedException.wrong();
        }

        return Long.valueOf(userId);
    }
}

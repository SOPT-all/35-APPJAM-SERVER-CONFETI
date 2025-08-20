package org.sopt.confeti.global.resolver.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.sopt.confeti.auth.jwt.JwtTokenValidator;
import org.sopt.confeti.global.annotation.AccessToken;
import org.sopt.confeti.global.annotation.RefreshToken;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String TOKEN_PREFIX = "Bearer ";
    private final JwtTokenValidator jwtTokenValidator;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(String.class) &&
                (parameter.hasParameterAnnotation(AccessToken.class) || parameter.hasParameterAnnotation(RefreshToken.class));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new ConfetiException(ErrorMessage.INTERNAL_SERVER_ERROR);
        }

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(token) || !token.startsWith(TOKEN_PREFIX)) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }

        String parsedToken = token.substring(TOKEN_PREFIX.length());
        jwtTokenValidator.validate(parsedToken);

        return parsedToken;
    }
}

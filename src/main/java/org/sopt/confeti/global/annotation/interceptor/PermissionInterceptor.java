package org.sopt.confeti.global.annotation.interceptor;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.auth.jwt.JwtTokenExtractor;
import org.sopt.confeti.auth.jwt.TokenParser;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Interceptor;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Interceptor
@RequiredArgsConstructor
public class PermissionInterceptor implements HandlerInterceptor {

    private final JwtTokenExtractor jwtTokenExtractor;
    private final TokenParser tokenParser;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        HandlerMethod method = (HandlerMethod) handler;
        Permission permission = method.getMethodAnnotation(Permission.class);
        if (permission == null) {
            return true;
        }

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        Role role;

        try {
            role = jwtTokenExtractor.getRole(tokenParser.getToken(token));
        } catch (NullPointerException e) {
            throw UnauthorizedException.empty();
        } catch (ExpiredJwtException e) {
            throw UnauthorizedException.expired();
        } catch (JwtException | IllegalArgumentException e) {
            throw UnauthorizedException.wrong();
        }

        Set<Role> permittedRoles = Set.of(permission.role());

        if (permittedRoles.contains(Role.ONBOARDING)) {
            if (role == Role.ONBOARDING) {
                return true;
            }
        }

        if (permittedRoles.contains(Role.GENERAL)) {
            if (role == Role.GENERAL) {
                return true;
            }
        }

        if (permittedRoles.contains(Role.ADMIN)) {
            if (role == Role.ADMIN) {
                return true;
            }
        }

        throw new ConfetiException(ErrorMessage.FORBIDDEN);
    }
}

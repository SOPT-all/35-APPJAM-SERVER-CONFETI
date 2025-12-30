package org.sopt.confeti.global.interceptor.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Interceptor;
import org.sopt.confeti.global.annotation.Permission;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.interceptor.CustomInterceptor;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

@Interceptor
public class PermissionInterceptor implements HandlerInterceptor, CustomInterceptor {

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull Object handler) {
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        HandlerMethod method = (HandlerMethod) handler;
        Permission permission = method.getMethodAnnotation(Permission.class);
        if (Objects.isNull(permission)) {
            return true;
        }

        return UserContext.getOptional()
            .map(info -> hasRequiredRole(info.role(), permission))
            .orElseGet(() -> !permission.required());
    }

    private boolean hasRequiredRole(Role role, Permission permission) {
        boolean permitted = Set.of(permission.role()).stream()
            .anyMatch(permittedRole -> permittedRole == role);

        if (!permitted) {
            throw new ConfetiException(ErrorMessage.FORBIDDEN);
        }

        return true;
    }
}

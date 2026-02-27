package org.sopt.confeti.global.interceptor.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Admin;
import org.sopt.confeti.global.annotation.Interceptor;
import org.sopt.confeti.global.interceptor.CustomInterceptor;
import org.sopt.confeti.global.exception.ForbiddenException;
import org.sopt.confeti.global.interceptor.auth.UserContext; 
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Interceptor
public class AdminInterceptor implements HandlerInterceptor, CustomInterceptor {
    
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull Object handler
    ) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod method = (HandlerMethod) handler;
        Admin adminMethod = method.getMethodAnnotation(Admin.class);
        Admin adminClass = method.getBeanType().getAnnotation(Admin.class);

        if (adminMethod != null || adminClass != null) {
            if (isAdmin()) {
                return true;
            }
            log.error(
                    "AdminInterceptor.preHandle : Admin API에 허용되지 않은 유저가 접근 시도. 유저 정보 : {}",
                    UserContext.get());
            throw new ForbiddenException(ErrorMessage.FORBIDDEN);
        }

        return true;
    }


    private boolean isAdmin() {
        return UserContext.getOptional()
            .map(userInfo -> userInfo.role() == Role.ADMIN)
            .orElse(false);
    }
}

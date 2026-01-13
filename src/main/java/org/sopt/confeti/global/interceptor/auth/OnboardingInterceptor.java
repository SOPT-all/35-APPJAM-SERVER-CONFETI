package org.sopt.confeti.global.interceptor.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Interceptor;
import org.sopt.confeti.global.annotation.Onboarding;
import org.sopt.confeti.global.exception.ForbiddenException;
import org.sopt.confeti.global.interceptor.CustomInterceptor;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Interceptor
public class OnboardingInterceptor implements HandlerInterceptor, CustomInterceptor {

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod method = (HandlerMethod) handler;
        Onboarding onboarding = method.getMethodAnnotation(Onboarding.class);
        if (onboarding == null) {
            // @Onboarding 어노테이션이 없는 API에 온보딩 유저가 접근하면 안된다.
            if (isOnboarding()) {
                log.error(
                    "OnboardingInterceptor.preHandle : 온보딩 유저가 허용되지 않은 API에 접근 시도. 유저 정보 : {}",
                    UserContext.get());
                throw new ForbiddenException(ErrorMessage.FORBIDDEN);
            }

            return true;
        }

        // @Onboarding 어노테이션이 있는 API에 온보딩 유저만 접근 가능하다. (편의성을 위해 일반 유저도 임시 허용)
        if (isNotOnboarding()) {
            log.error("OnboardingInterceptor.preHandle : 온보딩 API에 허용되지 않은 유저가 접근 시도. 유저 정보 : {}",
                UserContext.get());
            throw new ForbiddenException(ErrorMessage.FORBIDDEN);
        }

        return true;
    }

    private boolean isNotOnboarding() {
        Role userRole = UserContext.get().role();

        return userRole != Role.ONBOARDING
            && userRole != Role.GENERAL;
    }

    private boolean isOnboarding() {
        return UserContext.getOptional()
            .map(userInfo -> userInfo.role() == Role.ONBOARDING)
            .orElse(false);
    }
}

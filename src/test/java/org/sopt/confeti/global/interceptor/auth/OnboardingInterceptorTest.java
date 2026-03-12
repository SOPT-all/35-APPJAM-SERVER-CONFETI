package org.sopt.confeti.global.interceptor.auth;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.lang.reflect.Method;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.sopt.confeti.domain.user.constant.Role;
import org.sopt.confeti.global.annotation.Onboarding;
import org.sopt.confeti.global.exception.ForbiddenException;

class OnboardingInterceptorTest {

    private final OnboardingInterceptor onboardingInterceptor = new OnboardingInterceptor();
    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();
    private final TestController controller = new TestController();

    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    @Test
    void preHandle_onboardingApi_allowsAdmin() throws Exception {
        UserContext.set(userInfo(Role.ADMIN));

        assertThatCode(() -> onboardingInterceptor.preHandle(
            request,
            response,
            handlerMethod("onboardingApi")
        )).doesNotThrowAnyException();
    }

    @Test
    void preHandle_nonOnboardingApi_rejectsOnboardingUser() throws Exception {
        UserContext.set(userInfo(Role.ONBOARDING));

        assertThatThrownBy(() -> onboardingInterceptor.preHandle(
            request,
            response,
            handlerMethod("generalApi")
        )).isInstanceOf(ForbiddenException.class);
    }

    private HandlerMethod handlerMethod(String methodName) throws NoSuchMethodException {
        Method method = TestController.class.getDeclaredMethod(methodName);
        return new HandlerMethod(controller, method);
    }

    private UserInfo userInfo(Role role) {
        return UserInfo.builder()
            .id(1L)
            .role(role)
            .build();
    }

    private static class TestController {

        @Onboarding
        public void onboardingApi() {
        }

        public void generalApi() {
        }
    }
}

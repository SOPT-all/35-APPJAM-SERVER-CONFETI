package org.sopt.confeti.global.interceptor;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.sopt.confeti.global.interceptor.auth.AdminInterceptor;
import org.sopt.confeti.global.interceptor.auth.OnboardingInterceptor;
import org.sopt.confeti.global.interceptor.auth.PermissionInterceptor;
import org.sopt.confeti.global.interceptor.auth.UserContextInterceptor;
import org.springframework.core.Ordered;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class InterceptorOrder {

    private static final int NOT_FOUND_INDEX = -1;
    private static final int DEFAULT_INDEX = Ordered.LOWEST_PRECEDENCE;

    private static final List<Class<? extends CustomInterceptor>> interceptors = List.of(
        UserContextInterceptor.class,
        OnboardingInterceptor.class,
        PermissionInterceptor.class,
        AdminInterceptor.class
    );

    public static int getOrder(Class<? extends CustomInterceptor> interceptor) {
        int index = interceptors.indexOf(interceptor);

        if (index == NOT_FOUND_INDEX) {
            return DEFAULT_INDEX;
        }

        return index;
    }
}

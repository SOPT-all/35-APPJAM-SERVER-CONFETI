package org.sopt.confeti.global.interceptor.auth;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.task.TaskDecorator;

/**
 * @see UserContext
 */
public class UserContextDecorator implements TaskDecorator {

    @NotNull
    @Override
    public Runnable decorate(@NotNull Runnable runnable) {
        Optional<UserInfo> userInfo = UserContext.getOptional();

        return () -> {
            try {
                userInfo.ifPresent(UserContext::set);
                runnable.run();
            } finally {
                UserContext.clear();
            }
        };
    }
}

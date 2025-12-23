package org.sopt.confeti.global.interceptor.auth;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.sopt.confeti.domain.user.UserInfo;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.message.ErrorMessage;

/**
 * @see UserContextInterceptor
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserContext {

    private static final ThreadLocal<UserInfo> context = new ThreadLocal<>();

    public static void set(UserInfo userInfo) {
        context.set(userInfo);
    }

    public static UserInfo get() {
        UserInfo userInfo = context.get();
        if (userInfo == null) {
            throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED);
        }

        return context.get();
    }

    public static Optional<UserInfo> getOptional() {
        return Optional.ofNullable(context.get());
    }

    public static void clear() {
        context.remove();
    }
}

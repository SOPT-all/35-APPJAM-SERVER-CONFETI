package org.sopt.confeti.global.interceptor.auth;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
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

    /**
     * If UserInfo does not exist in the context, it throws an Unauthorized error.
     *
     * @return UserInfo
     * @throws UnauthorizedException This means that login is required.
     */
    public static UserInfo get() {
        UserInfo userInfo = context.get();
        if (userInfo == null) {
            throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED);
        }

        return userInfo;
    }

    /**
     * This means that login is optional.
     *
     * @return Optional<UserInfo>
     */
    public static Optional<UserInfo> getOptional() {
        return Optional.ofNullable(context.get());
    }

    public static void clear() {
        context.remove();
    }

    public static boolean exists() {
        return context.get() != null;
    }
}

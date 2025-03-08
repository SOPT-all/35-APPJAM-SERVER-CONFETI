package org.sopt.confeti.auth.command;

import org.sopt.confeti.api.auth.dto.LoginRequest;
import org.sopt.confeti.domain.user.OAuthProvider;

public record LoginCommand(
        OAuthProvider provider,
        String redirectUrl,
        String code,
        String name
) {
    public static LoginCommand from(LoginRequest request) {
        return new LoginCommand(
                request.provider(),
                request.redirectUrl(),
                request.code(),
                request.name()
        );
    }
}

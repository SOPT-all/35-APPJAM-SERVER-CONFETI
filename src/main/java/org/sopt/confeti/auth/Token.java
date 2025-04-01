package org.sopt.confeti.auth;

public record Token(
        String accessToken,
        String refreshToken
) {
}

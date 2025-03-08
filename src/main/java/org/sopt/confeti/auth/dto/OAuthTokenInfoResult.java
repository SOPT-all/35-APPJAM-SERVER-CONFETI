package org.sopt.confeti.auth.dto;

import org.sopt.confeti.auth.dto.apple.AppleTokenInfoResult;

public record OAuthTokenInfoResult(
        String accessToken,
        String refreshToken,
        long expiresIn
) {
    public static OAuthTokenInfoResult from(AppleTokenInfoResult token) {
        return new OAuthTokenInfoResult(
                token.accessToken(), token.refreshToken(), token.expiresIn()
        );
    }
}

package org.sopt.confeti.auth.dto.apple;

import io.jsonwebtoken.Claims;

public record AppleSocialInfoResult(
        String id,
        String name,
        AppleTokenInfoResult token
) {
        public static AppleSocialInfoResult of(Claims claims, String name, String accessToken, String refreshToken, String expiresIn) {
                return new AppleSocialInfoResult(
                        claims.getSubject(),
                        name,
                        AppleTokenInfoResult.create(accessToken, refreshToken, expiresIn)
                );
        }
}

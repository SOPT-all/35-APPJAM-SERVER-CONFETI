package org.sopt.confeti.domain.auth.dto.apple;

import io.jsonwebtoken.Claims;

public record AppleSocialInfoResult(
        String id,
        String name
) {
        public static AppleSocialInfoResult of(Claims claims, String name) {
                return new AppleSocialInfoResult(
                        claims.getSubject(),
                        name
                );
        }
}

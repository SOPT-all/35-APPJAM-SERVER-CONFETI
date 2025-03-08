package org.sopt.confeti.auth.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties (
        String secretKey,
        int accessTokenValidTime,
        int refreshTokenValidTime
){
}

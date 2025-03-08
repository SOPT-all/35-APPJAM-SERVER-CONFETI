package org.sopt.confeti.auth.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.constant.Role;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenExtractor {

    private static final String JWT_CLAIM_ROLE = "role";
    private static final String JWT_CLAIM_PROVIDER = "provider";

    private final JwtProperties jwtProperties;
    private final KeyGenerator keyGenerator;

    public String getSubject(String token) {
        return Jwts.parser()
                .verifyWith(keyGenerator.getKeyFromString(jwtProperties.secretKey()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Role getRole(String token) {
        return Jwts.parser()
                .verifyWith(keyGenerator.getKeyFromString(jwtProperties.secretKey()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(JWT_CLAIM_ROLE, Role.class);
    }

    public OAuthProvider getProvider(String token) {
        return Jwts.parser()
                .verifyWith(keyGenerator.getKeyFromString(jwtProperties.secretKey()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(JWT_CLAIM_PROVIDER, OAuthProvider.class);
    }
}

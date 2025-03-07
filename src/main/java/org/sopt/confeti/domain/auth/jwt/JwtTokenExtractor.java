package org.sopt.confeti.domain.auth.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.user.constant.Role;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenExtractor {

    private static final String JWT_CLAIM_ROLE = "role";

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
        String role = Jwts.parser()
                .verifyWith(keyGenerator.getKeyFromString(jwtProperties.secretKey()))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(JWT_CLAIM_ROLE, String.class);

        return Role.from(role);
    }
}

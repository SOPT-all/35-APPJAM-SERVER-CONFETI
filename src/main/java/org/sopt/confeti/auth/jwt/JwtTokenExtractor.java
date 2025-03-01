package org.sopt.confeti.auth.jwt;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenExtractor {

    private final JwtProperties jwtProperties;
    private final KeyGenerator keyGenerator;

    public String getSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(keyGenerator.getKeyFromString(jwtProperties.secretKey()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}

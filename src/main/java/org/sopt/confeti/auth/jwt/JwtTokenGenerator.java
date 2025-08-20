package org.sopt.confeti.auth.jwt;

import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.constant.Role;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenGenerator {

    private static final String JWT_TYPE = "JWT";
    private static final String JWT_IS_ACCESS_TOKEN = "isAccessToken";
    private static final String JWT_IS_REFRESH_TOKEN = "isRefreshToken";
    private static final String JWT_CLAIM_ROLE = "role";
    private static final String JWT_CLAIM_PROVIDER = "provider";

    private final JwtProperties jwtProperties;
    private final KeyGenerator keyGenerator;

    public String createAccessToken(String payload, Role role, OAuthProvider provider) {

        Date now = new Date();
        Map<String, Object> claims = new HashMap<>();

        claims.put(JWT_IS_ACCESS_TOKEN, true);
        claims.put(JWT_CLAIM_ROLE, role);
        claims.put(JWT_CLAIM_PROVIDER, provider);

        return Jwts.builder()
                .header().type(JWT_TYPE).and()
                .claims(claims)
                .subject(payload)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtProperties.accessTokenValidTime()))
                .signWith(keyGenerator.getKeyFromString(jwtProperties.secretKey()))
                .compact();
    }

    public String createRefreshToken(String payload, Role role, OAuthProvider provider) {

        Date now = new Date();
        Map<String, Object> claims = new HashMap<>();

        claims.put(JWT_IS_REFRESH_TOKEN, true);
        claims.put(JWT_CLAIM_ROLE, role);
        claims.put(JWT_CLAIM_PROVIDER, provider);

        return Jwts.builder()
                .header().type(JWT_TYPE).and()
                .claims(claims)
                .subject(payload)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + jwtProperties.refreshTokenValidTime()))
                .signWith(keyGenerator.getKeyFromString(jwtProperties.secretKey()))
                .compact();
    }

    public int getRefreshTokenValidTime() {
        return jwtProperties.refreshTokenValidTime();
    }
}

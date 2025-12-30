package org.sopt.confeti.auth.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.constant.Role;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenExtractor {

    private static final String JWT_IS_ACCESS_TOKEN = "isAccessToken";
    private static final String JWT_CLAIM_ROLE = "role";
    private static final String JWT_CLAIM_PROVIDER = "provider";

    private final JwtParser jwtParser;

    public JwtTokenExtractor(JwtProperties jwtProperties, KeyGenerator keyGenerator) {
        this.jwtParser = Jwts.parser()
            .verifyWith(keyGenerator.getKeyFromString(jwtProperties.secretKey()))
            .build();
    }

    public String getSubject(String token) throws JwtException, IllegalArgumentException {
        return jwtParser.parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public boolean isAccessToken(String token) throws JwtException, IllegalArgumentException {
        return jwtParser.parseSignedClaims(token)
            .getPayload()
            .get(JWT_IS_ACCESS_TOKEN, Boolean.class);
    }

    public Role getRole(String token) throws JwtException, IllegalArgumentException {
        String role = jwtParser.parseSignedClaims(token)
            .getPayload()
            .get(JWT_CLAIM_ROLE, String.class);

        return Role.from(role);
    }

    public OAuthProvider getProvider(String token) {
        String provider = jwtParser.parseSignedClaims(token)
            .getPayload()
            .get(JWT_CLAIM_PROVIDER, String.class);

        return OAuthProvider.valueOf(provider);
    }
}

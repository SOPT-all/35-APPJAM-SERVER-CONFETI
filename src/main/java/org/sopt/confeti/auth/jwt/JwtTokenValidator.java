package org.sopt.confeti.auth.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.sopt.confeti.global.exception.UnauthorizedException;


@Component
@RequiredArgsConstructor
public class JwtTokenValidator {

    private final KeyGenerator keyGenerator;
    private final JwtProperties jwtProperties;

    public void validate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(keyGenerator.getKeyFromString(jwtProperties.secretKey()))
                    .build()
                    .parseSignedClaims(token);
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException | UnsupportedJwtException e) {
            throw UnauthorizedException.wrong();
        } catch (ExpiredJwtException e) {
            throw UnauthorizedException.expired();
        }
    }

}

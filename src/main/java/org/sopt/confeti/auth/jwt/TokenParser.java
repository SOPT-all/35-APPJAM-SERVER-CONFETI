package org.sopt.confeti.auth.jwt;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenParser {
    private static final String PREFIX = "Bearer ";

    public String getToken(String token) {
        if (token.startsWith(PREFIX)) {
            return token.substring(PREFIX.length());
        } else {
            throw new UnauthorizedException(ErrorMessage.WRONG_TOKEN_REQUEST);
        }
    }
}

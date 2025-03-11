package org.sopt.confeti.auth.dto;

import org.sopt.confeti.auth.Token;

public record LoginResult (
        String accessToken,
        String refreshToken,
        boolean isOnboarding
){
    public static LoginResult from(Token token, boolean isOnboarding) {
        return new LoginResult(token.accessToken(), token.refreshToken(), isOnboarding);
    }}

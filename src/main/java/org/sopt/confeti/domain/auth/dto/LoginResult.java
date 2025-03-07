package org.sopt.confeti.domain.auth.dto;

import org.sopt.confeti.domain.auth.Token;

public record LoginResult (
        String accessToken,
        String refreshToken
){
    public static LoginResult from(Token token) {
        return new LoginResult(token.accessToken(), token.refreshToken());
    }}

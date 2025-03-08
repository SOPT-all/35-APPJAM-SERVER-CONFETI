package org.sopt.confeti.auth.dto;

import org.sopt.confeti.auth.Token;

public record LoginResult (
        String accessToken,
        String refreshToken
){
    public static LoginResult from(Token token) {
        return new LoginResult(token.accessToken(), token.refreshToken());
    }}

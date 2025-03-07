package org.sopt.confeti.domain.auth;

public record Token (
        String accessToken,
        String refreshToken
){
}

package org.sopt.confeti.auth.dto;

public record OAuthLoginParams (
        String redirectUrl,
        String code
){
}

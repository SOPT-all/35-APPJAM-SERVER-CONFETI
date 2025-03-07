package org.sopt.confeti.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoTokenResult(
        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("refresh_token")
        String refreshToken,

        @JsonProperty("token_type")
        String tokenType,

        @JsonProperty("expires_in")
        Long expiresIn
){
}

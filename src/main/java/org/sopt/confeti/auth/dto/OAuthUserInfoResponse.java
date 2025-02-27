package org.sopt.confeti.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OAuthUserInfoResponse(
        String id,
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
){
}

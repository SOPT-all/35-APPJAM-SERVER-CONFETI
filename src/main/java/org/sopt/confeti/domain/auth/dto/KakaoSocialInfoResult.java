package org.sopt.confeti.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoSocialInfoResult(
        String id,
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
){
}

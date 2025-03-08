package org.sopt.confeti.auth.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoProfile(

        @JsonProperty("nickname")
        String nickname,

        @JsonProperty("profile_image_url")
        String profileImageUrl
) {
}
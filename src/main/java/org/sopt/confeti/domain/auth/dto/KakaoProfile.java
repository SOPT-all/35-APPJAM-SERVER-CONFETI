package org.sopt.confeti.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoProfile(

        @JsonProperty("nickname")
        String nickname,

        @JsonProperty("profile_image_url")
        String profileImageUrl
) {
}
package org.sopt.confeti.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OAuthTokenResponse (
        @JsonProperty("access_token")
        String accessToken
){
}

package org.sopt.confeti.domain.auth.dto.apple;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AppleTokenResult(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("token_type")
        String tokenType,
        @JsonProperty("expires_in")
        String expiresIn,
        @JsonProperty("refresh_token")
        String refreshToken,
        @JsonProperty("id_token")
        String idToken
) {
}

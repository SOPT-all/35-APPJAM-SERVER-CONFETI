package org.sopt.confeti.api.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.sopt.confeti.domain.user.OAuthProvider;

public record LoginRequest (
        @NotNull
        OAuthProvider provider,
        String redirectUrl,
        @NotBlank
        String code,
        String name
) {
}

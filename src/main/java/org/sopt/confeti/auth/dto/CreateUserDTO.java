package org.sopt.confeti.auth.dto;

import org.sopt.confeti.domain.user.OAuthProvider;

public record CreateUserDTO(
        String id,
        OAuthProvider provider,
        String name,
        String profileImgUrl,
        OAuthTokenInfoResult token
) {
    public static CreateUserDTO of(OAuthProvider provider, OAuthSocialInfoResult socialInfo) {
        return new CreateUserDTO(
                socialInfo.id(),
                provider,
                socialInfo.name(),
                socialInfo.profileImgUrl(),
                socialInfo.token()
        );
    }

    public static CreateUserDTO of(OAuthProvider provider, OAuthSocialInfoResult socialInfo, String profileImgUrl) {
        return new CreateUserDTO(
                socialInfo.id(),
                provider,
                socialInfo.name(),
                profileImgUrl,
                socialInfo.token()
        );
    }
}

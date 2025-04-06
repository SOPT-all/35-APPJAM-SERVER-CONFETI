package org.sopt.confeti.auth.dto;

public record CreateUserDTO(
        String id,
        String name,
        String profileImgUrl,
        OAuthTokenInfoResult token
) {
    public static CreateUserDTO from(OAuthSocialInfoResult socialInfo) {
        return new CreateUserDTO(
                socialInfo.id(),
                socialInfo.name(),
                socialInfo.profileImgUrl(),
                socialInfo.token()
        );
    }

    public static CreateUserDTO of(OAuthSocialInfoResult socialInfo, String profileImgUrl) {
        return new CreateUserDTO(
                socialInfo.id(),
                socialInfo.name(),
                profileImgUrl,
                socialInfo.token()
        );
    }
}

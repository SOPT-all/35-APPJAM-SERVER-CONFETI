package org.sopt.confeti.auth.dto;

import org.sopt.confeti.domain.user.AuthUser;
import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.constant.Role;

public record CreateUserDTO(
        String id,
        OAuthProvider provider,
        String name,
        String profileImgUrl,
        OAuthTokenInfoResult token,
        Role role
) {

    public static CreateUserDTO of(OAuthProvider provider, OAuthSocialInfoResult socialInfo, String profileImgUrl) {
        return new CreateUserDTO(
                socialInfo.id(),
                provider,
                socialInfo.name(),
                profileImgUrl,
                socialInfo.token(),
                Role.ONBOARDING
        );
    }

    public CreateUserDTO withRole(Role role) {
        return new CreateUserDTO(id, provider, name, profileImgUrl, token, role);
    }

    public AuthUser toAuthUser() {
        return AuthUser.builder()
                .provider(provider)
                .socialId(id)
                .socialNickname(name)
                .socialProfile(profileImgUrl)
                .role(role)
                .build();
    }
}

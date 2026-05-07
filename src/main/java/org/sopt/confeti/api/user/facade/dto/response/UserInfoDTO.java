package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.UserFileInfo;
import org.sopt.confeti.global.interceptor.auth.UserInfo;

public record UserInfoDTO(
    long userId,
    String profileUrl,
    String name,
    OAuthProvider provider
) {

    public static UserInfoDTO of(UserInfo userInfo, UserFileInfo userFileInfo) {
        return new UserInfoDTO(
            userInfo.id(),
            userFileInfo.profileUrl(),
            userInfo.name(),
            userInfo.provider()
        );
    }
}

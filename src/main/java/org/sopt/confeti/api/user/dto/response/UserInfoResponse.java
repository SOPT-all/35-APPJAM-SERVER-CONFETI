package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserInfoDTO;
import org.sopt.confeti.domain.user.OAuthProvider;

public record UserInfoResponse(
    long userId,
    String profileUrl,
    String name,
    OAuthProvider provider
) {

    public static UserInfoResponse from(UserInfoDTO userInfoDTO) {
        return new UserInfoResponse(
            userInfoDTO.userId(),
            userInfoDTO.profileUrl(),
            userInfoDTO.name(),
            userInfoDTO.provider()
        );
    }
}

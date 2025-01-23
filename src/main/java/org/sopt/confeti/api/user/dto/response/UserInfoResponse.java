package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserInfoDTO;
import org.sopt.confeti.global.util.S3FileHandler;

public record UserInfoResponse (
        Long userId,
        String profileUrl,
        String username
) {
    public static UserInfoResponse of(final UserInfoDTO userInfoDTO, final S3FileHandler s3FileHandler) {
        return new UserInfoResponse(
                userInfoDTO.userId(),
                s3FileHandler.getFileUrl(userInfoDTO.profilePath()),
                userInfoDTO.username()
        );
    }
}
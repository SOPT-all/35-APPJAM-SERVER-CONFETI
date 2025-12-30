package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.interceptor.auth.UserInfo;
import org.sopt.confeti.global.util.S3FileHandler;

public record UserInfoResponse(
    Long userId,
    String profileUrl,
    String name,
    OAuthProvider provider
) {

    public static UserInfoResponse of(UserInfo userInfo, S3FileHandler s3FileHandler) {
        return new UserInfoResponse(
            userInfo.id(),
            s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.USER, FolderPath.PROFILE),
                userInfo.profilePath()).toString(),
            userInfo.name(),
            userInfo.provider()
        );
    }
}

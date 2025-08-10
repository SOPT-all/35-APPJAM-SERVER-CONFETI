package org.sopt.confeti.api.user.facade.dto.response;

import org.sopt.confeti.domain.user.OAuthProvider;
import org.sopt.confeti.domain.user.User;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record UserInfoDTO(
        long userId,
        String profileUrl,
        String name,
        OAuthProvider provider
) {
    public static UserInfoDTO of(User user, S3FileHandler s3FileHandler) {
        return new UserInfoDTO(
                user.getId(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.USER, FolderPath.PROFILE),
                        user.getProfilePath()
                ).toString(),
                user.getName(),
                user.getProvider()
        );
    }
}

package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserFavoritePerformanceDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record UserFavoritePerformanceResponse(
        long performanceId,
        String type,
        String title,
        String posterUrl
) {
    public static UserFavoritePerformanceResponse of(final UserFavoritePerformanceDTO performanceDTO, final S3FileHandler s3FileHandler) {
        FolderPath topFolder = FolderPath.getFolderPathByPerformanceType(performanceDTO.type());

        return new UserFavoritePerformanceResponse(
                performanceDTO.performanceId(),
                performanceDTO.type().getName(),
                performanceDTO.title(),
                s3FileHandler.getFileUrl(FolderPath.combine(topFolder, FolderPath.POSTER), performanceDTO.posterPath())
                        .toString()
        );
    }
}

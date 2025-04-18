package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserFavoritePerformanceAllDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.util.DateConvertor;
import org.sopt.confeti.global.util.S3FileHandler;

public record UserFavoritePerformanceAllResponse(
        long typeId,
        PerformanceType type,
        String title,
        String posterUrl,
        String startAt,
        String endAt,
        String area,
        boolean isFavorite
) {
    public static UserFavoritePerformanceAllResponse of(
            final UserFavoritePerformanceAllDTO userFavoritePerformanceAllDTO, final S3FileHandler s3FileHandler) {
        FolderPath topFolder = FolderPath.getFolderPathByPerformanceType(userFavoritePerformanceAllDTO.type());

        return new UserFavoritePerformanceAllResponse(
                userFavoritePerformanceAllDTO.typeId(),
                userFavoritePerformanceAllDTO.type(),
                userFavoritePerformanceAllDTO.title(),
                s3FileHandler.getFileUrl(FolderPath.combine(topFolder, FolderPath.POSTER),
                                userFavoritePerformanceAllDTO.posterPath())
                        .toString(),
                DateConvertor.convertToDefaultFormat(userFavoritePerformanceAllDTO.startAt()),
                DateConvertor.convertToDefaultFormat(userFavoritePerformanceAllDTO.endAt()),
                userFavoritePerformanceAllDTO.area(),
                true
        );
    }
}



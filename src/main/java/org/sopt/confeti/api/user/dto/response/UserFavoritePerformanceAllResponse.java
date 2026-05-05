package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserFavoritePerformanceAllDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.util.DateConvertor;

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

    public static UserFavoritePerformanceAllResponse from(
        final UserFavoritePerformanceAllDTO userFavoritePerformanceAllDTO) {
        return new UserFavoritePerformanceAllResponse(
            userFavoritePerformanceAllDTO.typeId(),
            userFavoritePerformanceAllDTO.type(),
            userFavoritePerformanceAllDTO.title(),
            userFavoritePerformanceAllDTO.posterUrl(),
            DateConvertor.convertToDefaultFormat(userFavoritePerformanceAllDTO.startAt()),
            DateConvertor.convertToDefaultFormat(userFavoritePerformanceAllDTO.endAt()),
            userFavoritePerformanceAllDTO.area(),
            true
        );
    }
}



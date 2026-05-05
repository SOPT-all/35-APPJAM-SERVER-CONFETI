package org.sopt.confeti.api.user.dto.response;

import org.sopt.confeti.api.user.facade.dto.response.UserFavoritePerformanceDTO;

public record UserFavoritePerformanceResponse(
    int index,
    long typeId,
    String type,
    String title,
    String posterUrl
) {

    public static UserFavoritePerformanceResponse of(
        final UserFavoritePerformanceDTO performanceDTO,
        final int index) {
        return new UserFavoritePerformanceResponse(
            index,
            performanceDTO.typeId(),
            performanceDTO.type().getName(),
            performanceDTO.title(),
            performanceDTO.posterUrl()
        );
    }
}

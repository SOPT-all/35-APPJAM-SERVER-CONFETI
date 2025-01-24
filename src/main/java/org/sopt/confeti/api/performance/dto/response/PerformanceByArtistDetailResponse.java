package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.PerformanceByArtistDetailDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.util.DateConvertor;
import org.sopt.confeti.global.util.S3FileHandler;

public record PerformanceByArtistDetailResponse(
        long performanceId,
        long typeId,
        PerformanceType type,
        String title,
        String performanceStartAt,
        String performanceEndAt,
        String posterUrl,
        String area,
        boolean isFavorite
) {
    public static PerformanceByArtistDetailResponse of(PerformanceByArtistDetailDTO performance, S3FileHandler s3FileHandler) {
        return new PerformanceByArtistDetailResponse(
                performance.performanceId(),
                performance.typeId(),
                performance.type(),
                performance.title(),
                DateConvertor.convertToLocalDate(performance.performanceStartAt()),
                DateConvertor.convertToLocalDate(performance.performanceEndAt()),
                s3FileHandler.getFileUrl(performance.posterPath()),
                performance.area(),
                performance.isFavorite()
        );
    }
}

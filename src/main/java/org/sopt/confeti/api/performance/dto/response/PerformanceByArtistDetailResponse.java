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
    public static PerformanceByArtistDetailResponse of(PerformanceByArtistDetailDTO performanceByArtistDetailDTO, S3FileHandler s3FileHandler) {
        return new PerformanceByArtistDetailResponse(
                performanceByArtistDetailDTO.performanceId(),
                performanceByArtistDetailDTO.typeId(),
                performanceByArtistDetailDTO.type(),
                performanceByArtistDetailDTO.title(),
                DateConvertor.convertToLocalDate(performanceByArtistDetailDTO.performanceStartAt()),
                DateConvertor.convertToLocalDate(performanceByArtistDetailDTO.performanceEndAt()),
                s3FileHandler.getFileUrl(performanceByArtistDetailDTO.posterPath()),
                performanceByArtistDetailDTO.area(),
                performanceByArtistDetailDTO.isFavorite()
        );
    }
}

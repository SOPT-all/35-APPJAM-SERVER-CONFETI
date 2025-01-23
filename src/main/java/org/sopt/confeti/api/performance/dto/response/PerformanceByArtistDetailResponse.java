package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.PerformanceByArtistListDTO;
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
    public static PerformanceByArtistDetailResponse of(PerformanceByArtistListDTO performanceByArtistListDTO, S3FileHandler s3FileHandler) {
        return new PerformanceByArtistDetailResponse(
                performanceByArtistListDTO.performanceId(),
                performanceByArtistListDTO.typeId(),
                performanceByArtistListDTO.type(),
                performanceByArtistListDTO.title(),
                DateConvertor.convertToLocalDate(performanceByArtistListDTO.performanceStartAt()),
                DateConvertor.convertToLocalDate(performanceByArtistListDTO.performanceEndAt()),
                s3FileHandler.getFileUrl(performanceByArtistListDTO.posterUrl()),
                performanceByArtistListDTO.area(),
                performanceByArtistListDTO.isFavorite()
        );
    }
}

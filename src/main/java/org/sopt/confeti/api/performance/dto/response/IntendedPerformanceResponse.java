package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.IntendedPerformanceDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.DateConvertor;
import org.sopt.confeti.global.util.S3FileHandler;

public record IntendedPerformanceResponse(
        long performanceId,
        String type,
        long typeId,
        String title,
        String posterUrl,
        String startAt,
        String endAt,
        String area,
        boolean isFavorite
) {
    public static IntendedPerformanceResponse of(IntendedPerformanceDTO performanceDTO, S3FileHandler s3FileHandler) {
        return new IntendedPerformanceResponse(
                performanceDTO.id(),
                performanceDTO.type().getType(),
                performanceDTO.typeId(),
                performanceDTO.title(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.getFolderPathByPerformanceType(performanceDTO.type()),
                                FolderPath.POSTER),
                        performanceDTO.posterPath()).toString(),
                DateConvertor.convertToDefaultFormat(performanceDTO.startAt()),
                DateConvertor.convertToDefaultFormat(performanceDTO.endAt()),
                performanceDTO.area(),
                performanceDTO.isFavorite()
        );
    }
}

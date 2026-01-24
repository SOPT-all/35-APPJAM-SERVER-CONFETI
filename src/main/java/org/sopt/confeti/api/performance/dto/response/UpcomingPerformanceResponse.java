package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.UpcomingPerformanceDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record UpcomingPerformanceResponse(
        long performanceId,
        String type,
        long typeId,
        String title,
        String posterUrl
) {
    public static UpcomingPerformanceResponse of(UpcomingPerformanceDTO upcomingPerformanceDTO,
                                                 S3FileHandler s3FileHandler) {
        return new UpcomingPerformanceResponse(
                upcomingPerformanceDTO.id(),
                upcomingPerformanceDTO.type().getName(),
                upcomingPerformanceDTO.typeId(),
                upcomingPerformanceDTO.title(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.getFolderPathByPerformanceType(upcomingPerformanceDTO.type()),
                                FolderPath.POSTER),
                        upcomingPerformanceDTO.posterPath()).toString()
        );
    }
}

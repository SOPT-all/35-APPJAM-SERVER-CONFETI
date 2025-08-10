package org.sopt.confeti.api.user.facade.dto.response;

import java.time.LocalDate;

import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record UpcomingFavoritePerformanceDTO(
        long id,
        PerformanceType type,
        String title,
        String posterUrl,
        LocalDate startAt,
        LocalDate endAt,
        String area
) {
    public static UpcomingFavoritePerformanceDTO of(Performance performance, S3FileHandler s3FileHandler) {
        return new UpcomingFavoritePerformanceDTO(
                performance.getId(),
                performance.getType(),
                performance.getTitle(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.getFolderPathByPerformanceType(performance.getType()), FolderPath.POSTER),
                        performance.getPosterPath()
                ).toString(),
                performance.getStartAt(),
                performance.getEndAt(),
                performance.getArea()
        );
    }
}


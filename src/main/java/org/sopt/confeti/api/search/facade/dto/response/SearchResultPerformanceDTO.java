package org.sopt.confeti.api.search.facade.dto.response;

import java.time.LocalDate;

import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;
import org.sopt.confeti.global.util.S3FileHandler;

public record SearchResultPerformanceDTO(
        long id,
        PerformanceType type,
        String title,
        String posterUrl,
        LocalDate startAt,
        LocalDate endAt,
        String area,
        boolean isFavorite
) {
    public static SearchResultPerformanceDTO of(Performance performance, boolean performanceFavorite, S3FileHandler s3FileHandler) {
        return new SearchResultPerformanceDTO(
                performance.getId(),
                performance.getType(),
                performance.getTitle(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.getFolderPathByPerformanceType(performance.getType()), FolderPath.POSTER),
                        performance.getPosterPath()
                ).toString(),
                performance.getStartAt(),
                performance.getEndAt(),
                performance.getArea(),
                performanceFavorite
        );
    }
}

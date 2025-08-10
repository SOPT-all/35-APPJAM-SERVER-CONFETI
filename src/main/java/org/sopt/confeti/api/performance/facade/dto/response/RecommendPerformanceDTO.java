package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record RecommendPerformanceDTO(
        long id,
        PerformanceType type,
        String title,
        String posterUrl
) {
    public static RecommendPerformanceDTO of(Performance performance, S3FileHandler s3FileHandler) {
        return new RecommendPerformanceDTO(
                performance.getId(),
                performance.getType(),
                performance.getTitle(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.getFolderPathByPerformanceType(performance.getType()), FolderPath.POSTER),
                        performance.getPosterPath()
                ).toString()
        );
    }
}
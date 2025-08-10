package org.sopt.confeti.api.user.facade.dto.response;

import java.time.LocalDate;

import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.domain.view.performance.Performance_DPRECATED;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;
import org.sopt.confeti.global.util.S3FileHandler;

public record UpcomingPerformanceDTO(
        long id,
        PerformanceType type,
        String title,
        String posterUrl,
        LocalDate startAt,
        LocalDate endAt,
        String area
) {
    public static UpcomingPerformanceDTO from(Performance_DPRECATED performanceDPRECATED) {
        return new UpcomingPerformanceDTO(
                performanceDPRECATED.getTypeId(),
                performanceDPRECATED.getType(),
                performanceDPRECATED.getTitle(),
                performanceDPRECATED.getPosterPath(),
                performanceDPRECATED.getStartAt(),
                performanceDPRECATED.getEndAt(),
                performanceDPRECATED.getArea()
        );
    }

    public static UpcomingPerformanceDTO of(Performance performance, S3FileHandler s3FileHandler) {
        return new UpcomingPerformanceDTO(
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

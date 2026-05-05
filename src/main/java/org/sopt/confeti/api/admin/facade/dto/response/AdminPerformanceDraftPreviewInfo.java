package org.sopt.confeti.api.admin.facade.dto.response;

import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftFileInfo;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftType;
import org.sopt.confeti.domain.performancedraft.application.PerformanceDraftParser;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftDto;

public record AdminPerformanceDraftPreviewInfo(
    Long id,
    PerformanceDraftType performanceDraftType,
    DraftStatus status,
    String posterUrl,
    String title,
    String area,
    String startAt
) {
    public static AdminPerformanceDraftPreviewInfo of(
        PerformanceDraftDto dto,
        PerformanceDraftParser parser,
        PerformanceDraftFileInfo fileInfo
    ) {
        String performanceData = dto.performanceData();
        return new AdminPerformanceDraftPreviewInfo(
            dto.id(),
            dto.performanceDraftType(),
            dto.status(),
            fileInfo.posterUrl(),
            parser.parseTitle(performanceData),
            parser.parseArea(performanceData),
            parser.parseStartAt(performanceData)
        );
    }
}

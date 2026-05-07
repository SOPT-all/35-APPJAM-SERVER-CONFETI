package org.sopt.confeti.api.admin.facade.dto.response;

import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftType;
import org.sopt.confeti.domain.performancedraft.application.PerformanceDraftParser;
import org.sopt.confeti.domain.performancedraft.application.dto.response.PerformanceDraftInfo;

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
        PerformanceDraftInfo info,
        PerformanceDraftParser parser
    ) {
        String performanceData = info.performanceData();
        return new AdminPerformanceDraftPreviewInfo(
            info.id(),
            info.performanceDraftType(),
            info.status(),
            info.posterUrl(),
            parser.parseTitle(performanceData),
            parser.parseArea(performanceData),
            parser.parseStartAt(performanceData)
        );
    }
}

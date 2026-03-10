package org.sopt.confeti.api.admin.facade.dto.response;

import org.sopt.confeti.domain.performancedraft.DraftStatus;
import org.sopt.confeti.domain.performancedraft.PerformanceDraft;
import org.sopt.confeti.domain.performancedraft.PerformanceDraftType;
import org.sopt.confeti.domain.performancedraft.application.PerformanceDraftParser;

public record AdminPerformanceDraftPreviewInfo(
    Long id,
    PerformanceDraftType performanceDraftType,
    DraftStatus status,
    String posterPath,
    String title,
    String area,
    String startAt
) {
    public static AdminPerformanceDraftPreviewInfo from(
        PerformanceDraft draft,
        PerformanceDraftParser parser
    ) {
        String performanceData = draft.getPerformanceData();
        return new AdminPerformanceDraftPreviewInfo(
            draft.getId(),
            draft.getPerformanceDraftType(),
            draft.getStatus(),
            draft.getPosterPath(),
            parser.parseTitle(performanceData),
            parser.parseArea(performanceData),
            parser.parseStartAt(performanceData)
        );
    }
}

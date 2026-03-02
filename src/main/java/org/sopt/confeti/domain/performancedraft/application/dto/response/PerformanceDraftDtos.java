package org.sopt.confeti.domain.performancedraft.application.dto.response;

import java.util.List;

public record PerformanceDraftDtos(
        List<PerformanceDraftDto> drafts
) {
    public static PerformanceDraftDtos from(List<PerformanceDraftDto> drafts) {
        return new PerformanceDraftDtos(drafts);
    }
}

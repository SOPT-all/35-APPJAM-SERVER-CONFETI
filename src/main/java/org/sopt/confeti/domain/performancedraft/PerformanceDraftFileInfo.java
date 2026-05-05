package org.sopt.confeti.domain.performancedraft;

import lombok.Builder;

@Builder
public record PerformanceDraftFileInfo(
    String posterUrl,
    String logoUrl
) {
}

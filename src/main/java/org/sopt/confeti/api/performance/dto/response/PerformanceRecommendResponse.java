package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceRecommendDTO;

public record PerformanceRecommendResponse(
        long typeId,
        String type,
        String title,
        String posterUrl,
        List<SongRecommendResponse> songs
) {
    public static PerformanceRecommendResponse from(PerformanceRecommendDTO performance) {
        return new PerformanceRecommendResponse(
                performance.typeId(),
                performance.type().getName(),
                performance.title(),
                performance.posterUrl(),
                performance.songs().stream()
                        .map(SongRecommendResponse::from)
                        .toList()
        );
    }
}

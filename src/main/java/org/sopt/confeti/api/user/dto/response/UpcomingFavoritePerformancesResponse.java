package org.sopt.confeti.api.user.dto.response;

import java.util.List;
import org.sopt.confeti.api.user.facade.dto.response.UpcomingFavoritePerformancesDTO;

public record UpcomingFavoritePerformancesResponse(
        List<UpcomingFavoritePerformanceResponse> performances) {
    public static UpcomingFavoritePerformancesResponse from(UpcomingFavoritePerformancesDTO performancesDTO) {
        return new UpcomingFavoritePerformancesResponse(
                performancesDTO.performances().stream()
                        .map(UpcomingFavoritePerformanceResponse::from)
                        .toList()
        );
    }
}

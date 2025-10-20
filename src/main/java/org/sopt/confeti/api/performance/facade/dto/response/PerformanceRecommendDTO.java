package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record PerformanceRecommendDTO(
        long typeId,
        PerformanceType type,
        String title,
        String posterPath,
        List<SongRecommendDTO> songs
) {
    public static PerformanceRecommendDTO of(PerformanceDTO performance, List<SongRecommendDTO> songsRecommend) {
        return new PerformanceRecommendDTO(
                performance.typeId(),
                performance.type(),
                performance.title(),
                performance.posterPath(),
                songsRecommend
        );
    }
}

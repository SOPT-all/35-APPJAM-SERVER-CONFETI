package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.RecommendMusicsDTO;
import org.sopt.confeti.global.common.constant.PerformanceType;

import java.util.List;

public record RecommendMusicsResponse(
        Long id,
        Long typeId,
        PerformanceType type,
        String title,
        List<RecommendMusicResponse> musicList
) {
    public static RecommendMusicsResponse from(RecommendMusicsDTO recommendMusicsDTO) {
        return new RecommendMusicsResponse(
                recommendMusicsDTO.id(),
                recommendMusicsDTO.typeId(),
                recommendMusicsDTO.type(),
                recommendMusicsDTO.title(),
                recommendMusicsDTO.musicList().stream()
                        .map(RecommendMusicResponse::from)
                        .toList()
        );
    }
}

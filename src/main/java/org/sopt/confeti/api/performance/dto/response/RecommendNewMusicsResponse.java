package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.RecommendNewMusicsDTO;
import java.util.List;

public record RecommendNewMusicsResponse(
        List<RecommendMusicResponse> musicList
) {
    public static RecommendNewMusicsResponse from(RecommendNewMusicsDTO musicListDTO) {
        return new RecommendNewMusicsResponse(
                musicListDTO.musicList().stream()
                        .map(RecommendMusicResponse::from)
                        .toList()
        );
    }
}
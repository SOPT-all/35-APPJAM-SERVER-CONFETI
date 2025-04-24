package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.RecommendMusicsDTO;
import java.util.List;

public record RecommendMusicsResponse(
        List<RecommendMusicResponse> musicList
) {
    public static RecommendMusicsResponse from(RecommendMusicsDTO musicListDTO) {
        return new RecommendMusicsResponse(
                musicListDTO.musicList().stream()
                        .map(RecommendMusicResponse::from)
                        .toList()
        );
    }
}
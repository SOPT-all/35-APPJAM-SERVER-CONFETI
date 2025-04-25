package org.sopt.confeti.api.performance.dto.response;

import java.util.List;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendMusicsDTO;

public record RecommendMusicsResponse(
        List<RecommendMusicResponse> musics
) {
    public static RecommendMusicsResponse from(RecommendMusicsDTO musicListDTO) {
        return new RecommendMusicsResponse(
                musicListDTO.musicList().stream()
                        .map(RecommendMusicResponse::from)
                        .toList()
        );
    }
}
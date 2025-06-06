package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.RecommendMusicDTO;

public record RecommendMusicResponse(
        String musicId,
        String artistName,
        String trackName,
        String artworkUrl,
        String previewUrl
) {
    public static RecommendMusicResponse from(RecommendMusicDTO recommendMusicDTO) {
        return new RecommendMusicResponse(
                recommendMusicDTO.id(),
                recommendMusicDTO.artistName(),
                recommendMusicDTO.trackName(),
                recommendMusicDTO.artworkUrl(),
                recommendMusicDTO.previewUrl()
        );
    }
}

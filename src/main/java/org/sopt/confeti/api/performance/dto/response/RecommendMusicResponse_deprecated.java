package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.RecommendSongDTO;

@Deprecated
public record RecommendMusicResponse_deprecated(
    String musicId,
    String artistName,
    String trackName,
    String artworkUrl,
    String previewUrl
) {

    public static RecommendMusicResponse_deprecated from(RecommendSongDTO recommendSongDTO) {
        return new RecommendMusicResponse_deprecated(
            recommendSongDTO.id(),
            recommendSongDTO.artistName(),
            recommendSongDTO.trackName(),
            recommendSongDTO.artworkUrl(),
            recommendSongDTO.previewUrl()
        );
    }
}

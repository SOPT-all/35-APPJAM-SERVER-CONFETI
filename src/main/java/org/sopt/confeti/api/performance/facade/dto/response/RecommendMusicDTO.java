package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;

public record RecommendMusicDTO(
        String id,
        String artistName,
        String trackName,
        String artworkUrl,
        String previewUrl
) {
    public static RecommendMusicDTO from(ConfetiMusic music) {
        return new RecommendMusicDTO(
                music.getId(),
                music.getArtistName(),
                music.getTrackName(),
                music.getArtworkUrl(),
                music.getPreviewUrl()
        );
    }
}

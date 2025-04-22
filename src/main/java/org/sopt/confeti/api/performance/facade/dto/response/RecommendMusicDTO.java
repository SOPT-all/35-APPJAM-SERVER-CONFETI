package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;

public record RecommendMusicDTO(
        String artistName,
        String title,
        String artWorkUrl,
        String previewUrl
) {
    public static RecommendMusicDTO from(ConfetiMusic music) {
        return new RecommendMusicDTO(
                music.getArtistName(),
                music.getTitle(),
                music.getArtworkUrl(),
                music.getPreviewUrl()
        );
    }
}

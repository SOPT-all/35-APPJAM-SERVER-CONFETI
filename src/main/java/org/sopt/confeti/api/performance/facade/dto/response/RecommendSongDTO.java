package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;

public record RecommendSongDTO(
    String id,
    String artistName,
    String trackName,
    String artworkUrl,
    String previewUrl
) {

    public static RecommendSongDTO from(ConfetiSong song) {
        return new RecommendSongDTO(
            song.getId(),
            song.getArtistName(),
            song.getTrackName(),
            song.getArtworkUrl(),
            song.getPreviewUrl()
        );
    }
}

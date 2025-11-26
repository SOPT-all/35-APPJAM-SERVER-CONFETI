package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;

public record SongRecommendDTO(
    String id,
    String songName,
    String artistName,
    String artworkUrl,
    String previewUrl
) {

    public static SongRecommendDTO from(ConfetiMusic song) {
        return new SongRecommendDTO(
            song.getId(),
            song.getTrackName(),
            song.getArtistName(),
            song.getArtworkUrl(),
            song.getPreviewUrl()
        );
    }
}

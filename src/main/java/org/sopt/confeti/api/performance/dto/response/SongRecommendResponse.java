package org.sopt.confeti.api.performance.dto.response;

import org.sopt.confeti.api.performance.facade.dto.response.SongRecommendDTO;

public record SongRecommendResponse(
    String songId,
    String songName,
    String artistName,
    String artworkUrl,
    String previewUrl
) {

    public static SongRecommendResponse from(SongRecommendDTO song) {
        return new SongRecommendResponse(
            song.id(),
            song.songName(),
            song.artistName(),
            song.artworkUrl(),
            song.previewUrl()
        );
    }
}

package org.sopt.confeti.api.setlist.dto.response;

import org.sopt.confeti.domain.setlist.SetlistMusic;

public record SetlistMusicResponse(
        Long musicId,
        String trackId,
        String artistName,
        String trackName,
        String artworkUrl,
        String previewUrl,
        int orders
) {
    public static SetlistMusicResponse create(SetlistMusic music) {
        return new SetlistMusicResponse(
                music.getId(),
                music.getTrackId(),
                music.getArtistName(),
                music.getTrackName(),
                music.getArtworkUrl(),
                music.getPreviewUrl(),
                music.getOrders()
        );
    }
}

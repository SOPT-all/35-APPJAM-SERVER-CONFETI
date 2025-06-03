package org.sopt.confeti.api.setlist.dto.response;

import org.sopt.confeti.domain.setlist.SetlistMusic;

public record SetlistMusicResponse(
        Long setlistMusicId,
        String musicId,
        String artistName,
        String trackName,
        String artworkUrl,
        String previewUrl,
        int orders
) {
    public static SetlistMusicResponse from(SetlistMusic music) {
        return new SetlistMusicResponse(
                music.getId(),
                music.getMusicId(),
                music.getArtistName(),
                music.getTrackName(),
                music.getArtworkUrl(),
                music.getPreviewUrl(),
                music.getOrders()
        );
    }
}

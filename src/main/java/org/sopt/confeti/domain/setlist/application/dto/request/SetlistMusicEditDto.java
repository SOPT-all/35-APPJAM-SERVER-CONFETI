package org.sopt.confeti.domain.setlist.application.dto.request;

import org.sopt.confeti.domain.setlist.SetlistMusic;

public record SetlistMusicEditDto(
        Long musicId,
        String trackId,
        String artistName,
        String trackName,
        String artworkUrl,
        String previewUrl,
        int orders
) {
    public static SetlistMusicEditDto from(SetlistMusic music) {
        return new SetlistMusicEditDto(
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

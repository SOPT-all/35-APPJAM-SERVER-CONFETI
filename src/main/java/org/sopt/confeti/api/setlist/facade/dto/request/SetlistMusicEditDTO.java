package org.sopt.confeti.api.setlist.facade.dto.request;

import org.sopt.confeti.domain.setlist.SetlistMusic;

public record SetlistMusicEditDTO(
        Long musicId,
        String trackId,
        String artistName,
        String trackName,
        String artworkUrl,
        String previewUrl,
        int orders
) {
    public static SetlistMusicEditDTO from(SetlistMusic music) {
        return new SetlistMusicEditDTO(
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

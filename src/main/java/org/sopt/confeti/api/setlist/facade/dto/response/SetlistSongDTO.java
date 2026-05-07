package org.sopt.confeti.api.setlist.facade.dto.response;

import org.sopt.confeti.domain.setlist.SetlistSong;

public record SetlistSongDTO(
    long setlistSongId,
    String songId,
    String artistName,
    String trackName,
    String artworkUrl,
    String previewUrl,
    int orders
) {

    public static SetlistSongDTO from(SetlistSong song) {
        return new SetlistSongDTO(
            song.getId(),
            song.getSongId(),
            song.getArtistName(),
            song.getTrackName(),
            song.getArtworkUrl(),
            song.getPreviewUrl(),
            song.getOrders()
        );
    }
}

package org.sopt.confeti.api.setlist.dto.response;

import org.sopt.confeti.domain.setlist.SetlistSong;

public record SetlistSongResponse(
    Long setlistSongId,
    String songId,
    String artistName,
    String trackName,
    String artworkUrl,
    String previewUrl,
    int orders
) {

    public static SetlistSongResponse from(SetlistSong song) {
        return new SetlistSongResponse(
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

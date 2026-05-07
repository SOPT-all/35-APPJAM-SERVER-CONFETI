package org.sopt.confeti.api.setlist.dto.response;

import org.sopt.confeti.api.setlist.facade.dto.response.SetlistSongDTO;

public record SetlistSongResponse(
    long setlistSongId,
    String songId,
    String artistName,
    String trackName,
    String artworkUrl,
    String previewUrl,
    int orders
) {

    public static SetlistSongResponse from(SetlistSongDTO song) {
        return new SetlistSongResponse(
            song.setlistSongId(),
            song.songId(),
            song.artistName(),
            song.trackName(),
            song.artworkUrl(),
            song.previewUrl(),
            song.orders()
        );
    }
}

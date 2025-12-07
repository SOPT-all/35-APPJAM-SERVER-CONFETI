package org.sopt.confeti.api.setlist.facade.dto.request;

import org.sopt.confeti.domain.setlist.SetlistSong;
import org.sopt.confeti.global.annotation.RedisSerializable;

@RedisSerializable
public record SetlistSongEditDTO(
    Long setlistSongId,
    String songId,
    String artistName,
    String trackName,
    String artworkUrl,
    String previewUrl,
    int orders
) {

    public static SetlistSongEditDTO from(SetlistSong song) {
        return new SetlistSongEditDTO(
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

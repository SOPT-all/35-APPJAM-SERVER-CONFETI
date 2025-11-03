package org.sopt.confeti.api.setlist.facade.dto.request;

import org.sopt.confeti.domain.setlist.SetlistMusic;
import org.sopt.confeti.global.annotation.RedisSerializable;

@RedisSerializable
public record SetlistMusicEditDTO(
        Long setlistMusicId,
        String musicId,
        String artistName,
        String trackName,
        String artworkUrl,
        String previewUrl,
        int orders
) {
    public static SetlistMusicEditDTO from(SetlistMusic music) {
        return new SetlistMusicEditDTO(
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

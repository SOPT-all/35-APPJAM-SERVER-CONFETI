package org.sopt.confeti.api.setlist.dto.response;

import org.sopt.confeti.domain.setlist.SetlistSong;

@Deprecated
public record SetlistSongResponse_deprecated(
    Long setlistMusicId,
    String musicId,
    String artistName,
    String trackName,
    String artworkUrl,
    String previewUrl,
    int orders
) {

    public static SetlistSongResponse_deprecated from(SetlistSong music) {
        return new SetlistSongResponse_deprecated(
            music.getId(),
            music.getSongId(),
            music.getArtistName(),
            music.getTrackName(),
            music.getArtworkUrl(),
            music.getPreviewUrl(),
            music.getOrders()
        );
    }
}

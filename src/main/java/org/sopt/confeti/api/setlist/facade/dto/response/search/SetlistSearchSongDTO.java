package org.sopt.confeti.api.setlist.facade.dto.response.search;

import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;

public record SetlistSearchSongDTO(
    String id,
    String trackName,
    String artistName,
    String artworkUrl,
    String previewUrl
) {

    public static SetlistSearchSongDTO from(ConfetiSong song) {
        return new SetlistSearchSongDTO(
            song.getId(),
            song.getTrackName(),
            song.getArtistName(),
            song.getArtworkUrl(),
            song.getPreviewUrl()
        );
    }
}

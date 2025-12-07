package org.sopt.confeti.api.setlist.facade.dto.response.search;

import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;

public record SetlistSearchArtistSongDTO(
    String id,
    String trackName,
    String artistName,
    String artworkUrl,
    String previewUrl
) {

    public static SetlistSearchArtistSongDTO from(ConfetiSong song) {
        return new SetlistSearchArtistSongDTO(
            song.getId(),
            song.getTrackName(),
            song.getArtistName(),
            song.getArtworkUrl(),
            song.getPreviewUrl()
        );
    }
}

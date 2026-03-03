package org.sopt.confeti.api.search.facade.dto.response;

import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;

public record SearchResultSongDTO(
    String songId,
    String songName,
    String artistName,
    String artworkUrl,
    String previewUrl
) {

    public static SearchResultSongDTO from(ConfetiSong song) {
        return new SearchResultSongDTO(
            song.getId(),
            song.getTrackName(),
            song.getArtistName(),
            song.getArtworkUrl(),
            song.getPreviewUrl()
        );
    }
}

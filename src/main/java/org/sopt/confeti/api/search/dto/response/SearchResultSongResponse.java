package org.sopt.confeti.api.search.dto.response;

import org.sopt.confeti.api.search.facade.dto.response.SearchResultSongDTO;

public record SearchResultSongResponse(
    String songId,
    String songName,
    String artistName,
    String artworkUrl,
    String previewUrl
) {

    public static SearchResultSongResponse from(SearchResultSongDTO song) {
        return new SearchResultSongResponse(
            song.songId(),
            song.songName(),
            song.artistName(),
            song.artworkUrl(),
            song.previewUrl()
        );
    }
}

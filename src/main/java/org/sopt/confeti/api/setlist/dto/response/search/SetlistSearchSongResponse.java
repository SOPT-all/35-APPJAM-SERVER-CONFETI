package org.sopt.confeti.api.setlist.dto.response.search;

import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchSongDTO;

public record SetlistSearchSongResponse(
    String songId,
    String trackName,
    String artistName,
    String artworkUrl,
    String previewUrl
) {

    public static SetlistSearchSongResponse from(SetlistSearchSongDTO artistSong) {
        return new SetlistSearchSongResponse(
            artistSong.id(),
            artistSong.trackName(),
            artistSong.artistName(),
            artistSong.artworkUrl(),
            artistSong.previewUrl()
        );
    }
}

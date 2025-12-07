package org.sopt.confeti.api.setlist.dto.response.search;

import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchArtistSongDTO;

public record SetlistSearchArtistSongResponse(
    String songId,
    String trackName,
    String artistName,
    String artworkUrl,
    String previewUrl
) {

    public static SetlistSearchArtistSongResponse from(
        SetlistSearchArtistSongDTO artistSong) {
        return new SetlistSearchArtistSongResponse(
            artistSong.id(),
            artistSong.trackName(),
            artistSong.artistName(),
            artistSong.artworkUrl(),
            artistSong.previewUrl()
        );
    }
}

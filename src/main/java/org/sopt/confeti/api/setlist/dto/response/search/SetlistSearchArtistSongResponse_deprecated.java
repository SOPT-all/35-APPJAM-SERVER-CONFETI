package org.sopt.confeti.api.setlist.dto.response.search;

import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchArtistSongDTO;

@Deprecated
public record SetlistSearchArtistSongResponse_deprecated(
    String musicId,
    String trackName,
    String artistName,
    String artworkUrl,
    String previewUrl
) {

    public static SetlistSearchArtistSongResponse_deprecated from(
        SetlistSearchArtistSongDTO artistSong) {
        return new SetlistSearchArtistSongResponse_deprecated(
            artistSong.id(),
            artistSong.trackName(),
            artistSong.artistName(),
            artistSong.artworkUrl(),
            artistSong.previewUrl()
        );
    }
}

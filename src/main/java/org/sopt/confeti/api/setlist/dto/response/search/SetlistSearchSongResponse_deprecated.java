package org.sopt.confeti.api.setlist.dto.response.search;

import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchSongDTO;

@Deprecated
public record SetlistSearchSongResponse_deprecated(
    String musicId,
    String trackName,
    String artistName,
    String artworkUrl,
    String previewUrl
) {

    public static SetlistSearchSongResponse_deprecated from(SetlistSearchSongDTO artistSong) {
        return new SetlistSearchSongResponse_deprecated(
            artistSong.id(),
            artistSong.trackName(),
            artistSong.artistName(),
            artistSong.artworkUrl(),
            artistSong.previewUrl()
        );
    }
}

package org.sopt.confeti.api.setlist.dto.response.search;

import java.util.List;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchArtistSongsDTO;

public record SetlistSearchArtistSongsResponse(
    int nextOffset,
    boolean isLast,
    List<SetlistSearchArtistSongResponse> songs
) {

    public static SetlistSearchArtistSongsResponse from(SetlistSearchArtistSongsDTO artistSongs) {
        return new SetlistSearchArtistSongsResponse(
            artistSongs.nextOffset(),
            artistSongs.isLast(),
            artistSongs.songs().stream()
                .map(SetlistSearchArtistSongResponse::from)
                .toList()
        );
    }
}

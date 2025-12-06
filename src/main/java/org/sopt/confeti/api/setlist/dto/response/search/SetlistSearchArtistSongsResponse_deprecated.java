package org.sopt.confeti.api.setlist.dto.response.search;

import java.util.List;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchArtistSongsDTO;

@Deprecated
public record SetlistSearchArtistSongsResponse_deprecated(
    int nextOffset,
    boolean isLast,
    List<SetlistSearchArtistSongResponse_deprecated> musics
) {

    public static SetlistSearchArtistSongsResponse_deprecated from(SetlistSearchArtistSongsDTO artistSongs) {
        return new SetlistSearchArtistSongsResponse_deprecated(
            artistSongs.nextOffset(),
            artistSongs.isLast(),
            artistSongs.songs().stream()
                .map(SetlistSearchArtistSongResponse_deprecated::from)
                .toList()
        );
    }
}

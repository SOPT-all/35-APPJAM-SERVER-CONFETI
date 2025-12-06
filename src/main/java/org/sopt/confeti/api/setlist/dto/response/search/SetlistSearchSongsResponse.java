package org.sopt.confeti.api.setlist.dto.response.search;

import java.util.List;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchSongsDTO;

public record SetlistSearchSongsResponse(
    int nextOffset,
    boolean isLast,
    List<SetlistSearchSongResponse> songs
) {

    public static SetlistSearchSongsResponse from(SetlistSearchSongsDTO artistSongs) {
        return new SetlistSearchSongsResponse(
            artistSongs.nextOffset(),
            artistSongs.isLast(),
            artistSongs.songs().stream()
                .map(SetlistSearchSongResponse::from)
                .toList()
        );
    }
}

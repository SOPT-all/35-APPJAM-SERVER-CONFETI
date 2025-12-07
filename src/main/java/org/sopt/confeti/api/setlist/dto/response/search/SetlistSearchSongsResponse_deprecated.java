package org.sopt.confeti.api.setlist.dto.response.search;

import java.util.List;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchSongsDTO;

@Deprecated
public record SetlistSearchSongsResponse_deprecated(
    int nextOffset,
    boolean isLast,
    List<SetlistSearchSongResponse_deprecated> musics
) {

    public static SetlistSearchSongsResponse_deprecated from(SetlistSearchSongsDTO artistMusics) {
        return new SetlistSearchSongsResponse_deprecated(
            artistMusics.nextOffset(),
            artistMusics.isLast(),
            artistMusics.songs().stream()
                .map(SetlistSearchSongResponse_deprecated::from)
                .toList()
        );
    }
}

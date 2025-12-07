package org.sopt.confeti.api.setlist.facade.dto.response.search;

import java.util.List;
import org.sopt.confeti.global.util.music.dto.music.SongPage;

public record SetlistSearchArtistSongsDTO(
    int nextOffset,
    boolean isLast,
    List<SetlistSearchArtistSongDTO> songs
) {

    public static SetlistSearchArtistSongsDTO from(SongPage songPage) {
        return new SetlistSearchArtistSongsDTO(
            songPage.getNextOffset(),
            songPage.isLast(),
            songPage.getSongs().stream()
                .map(SetlistSearchArtistSongDTO::from)
                .toList()
        );
    }
}

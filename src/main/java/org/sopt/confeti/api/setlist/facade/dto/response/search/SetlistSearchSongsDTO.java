package org.sopt.confeti.api.setlist.facade.dto.response.search;

import java.util.List;
import org.sopt.confeti.global.util.music.dto.music.SongPage;

public record SetlistSearchSongsDTO(
    int nextOffset,
    boolean isLast,
    List<SetlistSearchSongDTO> songs
) {

    public static SetlistSearchSongsDTO from(SongPage songPage) {
        return new SetlistSearchSongsDTO(
            songPage.getNextOffset(),
            songPage.isLast(),
            songPage.getSongs().stream()
                .map(SetlistSearchSongDTO::from)
                .toList()
        );
    }
}

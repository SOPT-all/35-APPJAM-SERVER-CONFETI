package org.sopt.confeti.api.setlist.facade.dto.response.search;

import java.util.List;
import org.sopt.confeti.global.util.music.dto.music.MusicPage;

public record SetlistSearchArtistMusicsDTO(
        int nextOffset,
        boolean isLast,
        List<SetlistSearchArtistMusicDTO> musics
) {
    public static SetlistSearchArtistMusicsDTO from(MusicPage musicPage) {
        return new SetlistSearchArtistMusicsDTO(
                musicPage.getNextOffset(),
                musicPage.isLast(),
                musicPage.getMusics().stream()
                        .map(SetlistSearchArtistMusicDTO::from)
                        .toList()
        );
    }
}

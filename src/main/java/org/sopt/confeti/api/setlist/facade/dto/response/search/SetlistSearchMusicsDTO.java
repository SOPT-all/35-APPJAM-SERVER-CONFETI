package org.sopt.confeti.api.setlist.facade.dto.response.search;

import java.util.List;
import org.sopt.confeti.global.util.music.dto.music.MusicPage;

public record SetlistSearchMusicsDTO(
        int nextOffset,
        boolean isLast,
        List<SetlistSearchMusicDTO> musics
) {
    public static SetlistSearchMusicsDTO from(MusicPage musicPage) {
        return new SetlistSearchMusicsDTO(
                musicPage.getNextOffset(),
                musicPage.isLast(),
                musicPage.getMusics().stream()
                        .map(SetlistSearchMusicDTO::from)
                        .toList()
        );
    }
}

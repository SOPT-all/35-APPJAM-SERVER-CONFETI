package org.sopt.confeti.api.setlist.dto.response.search;

import java.util.List;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchMusicsDTO;

public record SetlistSearchMusicsResponse(
        int nextOffset,
        boolean isLast,
        List<SetlistSearchMusicResponse> musics
) {
    public static SetlistSearchMusicsResponse from(SetlistSearchMusicsDTO artistMusics) {
        return new SetlistSearchMusicsResponse(
                artistMusics.nextOffset(),
                artistMusics.isLast(),
                artistMusics.musics().stream()
                        .map(SetlistSearchMusicResponse::from)
                        .toList()
        );
    }
}

package org.sopt.confeti.api.setlist.dto.response.search;

import java.util.List;
import org.sopt.confeti.api.setlist.facade.dto.response.search.SetlistSearchArtistMusicsDTO;

public record SetlistSearchArtistMusicsResponse(
        int nextOffset,
        boolean isLast,
        List<SetlistSearchArtistMusicResponse> musics
) {
    public static SetlistSearchArtistMusicsResponse from(SetlistSearchArtistMusicsDTO artistMusics) {
        return new SetlistSearchArtistMusicsResponse(
                artistMusics.nextOffset(),
                artistMusics.isLast(),
                artistMusics.musics().stream()
                        .map(SetlistSearchArtistMusicResponse::from)
                        .toList()
        );
    }
}

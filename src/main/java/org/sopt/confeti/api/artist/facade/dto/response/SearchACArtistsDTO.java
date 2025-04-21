package org.sopt.confeti.api.artist.facade.dto.response;

import java.util.List;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record SearchACArtistsDTO(
        List<SearchACArtistDTO> artists
) {
    public static SearchACArtistsDTO from(List<ConfetiArtist> artists) {
        return new SearchACArtistsDTO(
                artists.stream()
                        .map(SearchACArtistDTO::from)
                        .toList()
        );
    }
}

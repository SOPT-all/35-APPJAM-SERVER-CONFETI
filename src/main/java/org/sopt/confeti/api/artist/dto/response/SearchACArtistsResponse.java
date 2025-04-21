package org.sopt.confeti.api.artist.dto.response;

import java.util.List;
import org.sopt.confeti.api.artist.facade.dto.response.SearchACArtistsDTO;

public record SearchACArtistsResponse(
        List<SearchACArtistResponse> artists
) {
    public static SearchACArtistsResponse from(SearchACArtistsDTO artistsDTO) {
        return new SearchACArtistsResponse(
                artistsDTO.artists().stream()
                        .map(SearchACArtistResponse::from)
                        .toList()
        );
    }
}

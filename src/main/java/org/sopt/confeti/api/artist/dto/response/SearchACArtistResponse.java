package org.sopt.confeti.api.artist.dto.response;

import org.sopt.confeti.api.artist.facade.dto.response.SearchACArtistDTO;

public record SearchACArtistResponse(
        String artistId,
        String name,
        String profileUrl
) {
    public static SearchACArtistResponse from(SearchACArtistDTO artistDTO) {
        return new SearchACArtistResponse(
                artistDTO.id(),
                artistDTO.name(),
                artistDTO.profileUrl()
        );
    }
}

package org.sopt.confeti.api.artist.dto.response;

import org.sopt.confeti.api.artist.facade.dto.response.SearchArtistDTO;

public record SearchArtistResponse(
        SearchArtistSingleResponse artist
) {
    public static SearchArtistResponse from(final SearchArtistDTO searchArtistDTO) {
        return new SearchArtistResponse(
                SearchArtistSingleResponse.from(searchArtistDTO)
        );
    }
}

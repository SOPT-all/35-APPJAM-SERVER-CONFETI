package org.sopt.confeti.api.search.dto.response;

import org.sopt.confeti.api.search.facade.dto.response.SearchResultArtistDTO;

public record SearchResultArtistResponse(
        String artistId,
        String name,
        String profileUrl,
        boolean isFavorite
) {
    public static SearchResultArtistResponse from(SearchResultArtistDTO artist) {
        return new SearchResultArtistResponse(
                artist.artistId(),
                artist.name(),
                artist.profileUrl(),
                artist.isFavorite()
        );
    }
}

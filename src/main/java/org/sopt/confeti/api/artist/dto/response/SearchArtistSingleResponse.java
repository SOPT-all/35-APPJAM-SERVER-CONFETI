package org.sopt.confeti.api.artist.dto.response;

import org.sopt.confeti.api.artist.facade.dto.response.SearchArtistDTO;

public record SearchArtistSingleResponse(
        String artistId,
        String name,
        String profileUrl,
        String recentAlbumName,
        boolean isFavorite
) {
    public static SearchArtistSingleResponse from(final SearchArtistDTO searchArtistDTO) {
        return new SearchArtistSingleResponse(
                searchArtistDTO.artistId(),
                searchArtistDTO.name(),
                searchArtistDTO.profileUrl(),
                searchArtistDTO.recentAlbumName(),
                searchArtistDTO.isFavorite()
        );
    }
}

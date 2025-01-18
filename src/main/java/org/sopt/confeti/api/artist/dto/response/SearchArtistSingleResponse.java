package org.sopt.confeti.api.artist.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.api.artist.facade.dto.response.SearchArtistDTO;
import org.sopt.confeti.global.util.DateConvertor;

public record SearchArtistSingleResponse(
        String artistId,
        String name,
        String profileUrl,
        String latestReleaseAt,
        boolean isFavorite,
        boolean isMultipleArtists
) {
    public static SearchArtistSingleResponse from(final SearchArtistDTO searchArtistDTO) {
        return new SearchArtistSingleResponse(
                searchArtistDTO.artistId(),
                searchArtistDTO.name(),
                searchArtistDTO.profileUrl(),
                DateConvertor.convert(searchArtistDTO.latestReleaseAt()),
                searchArtistDTO.isFavorite(),
                searchArtistDTO.isMultipleArtists()
        );
    }
}

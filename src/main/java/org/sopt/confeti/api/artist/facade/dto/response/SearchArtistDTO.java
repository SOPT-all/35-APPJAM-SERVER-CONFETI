package org.sopt.confeti.api.artist.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.global.util.artistsearcher.ConfetiArtist;

public record SearchArtistDTO(
        String artistId,
        String name,
        String profileUrl,
        LocalDate latestReleaseAt,
        boolean isFavorite,
        boolean isMultipleArtists
) {
    private static final boolean fixedIsMultipleArtists = false;

    public static SearchArtistDTO from(final ConfetiArtist confetiArtist, final boolean isFavorite) {
        return new SearchArtistDTO(
                confetiArtist.getArtistId(),
                confetiArtist.getName(),
                confetiArtist.getProfileUrl(),
                confetiArtist.getLatestReleaseAt(),
                isFavorite,
                fixedIsMultipleArtists
        );
    }
}

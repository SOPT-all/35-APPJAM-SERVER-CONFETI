package org.sopt.confeti.api.performance.facade.dto.response;

import org.sopt.confeti.domain.festival_artist.FestivalArtist;
import org.sopt.confeti.domain.music.artist.Artist;

public record FestivalDetailArtistDTO(
    String artistId,
    String name,
    String profileUrl
) {

    public static FestivalDetailArtistDTO from(final FestivalArtist festivalArtist) {
        Artist artist = festivalArtist.getArtist();
        return new FestivalDetailArtistDTO(
            artist.getId(),
            artist.getName(),
            artist.getProfileUrl()
        );
    }
}

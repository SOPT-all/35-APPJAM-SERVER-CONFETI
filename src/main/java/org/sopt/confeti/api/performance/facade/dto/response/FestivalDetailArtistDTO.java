package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.festivalartist.FestivalArtist;
import org.sopt.confeti.global.util.artistsearcher.ConfetiArtist;

public record FestivalDetailArtistDTO(
        String artistId,
        String name,
        String profileUrl,
        LocalDate latestReleaseAt
) {
    public static FestivalDetailArtistDTO from(final FestivalArtist festivalArtist) {
        ConfetiArtist confetiArtist = festivalArtist.getArtist();
        return new FestivalDetailArtistDTO(
                confetiArtist.getArtistId(),
                confetiArtist.getName(),
                confetiArtist.getProfileUrl(),
                confetiArtist.getLatestReleaseAt()
        );
    }
}

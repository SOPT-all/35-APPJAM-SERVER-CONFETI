package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.concertartist.ConcertArtist;
import org.sopt.confeti.global.util.artistsearcher.ConfetiArtist;

public record ConcertArtistDTO(
        String artistId,
        String name,
        String profileUrl,
        LocalDate latestReleaseAt
) {
    public static ConcertArtistDTO of(final ConcertArtist concertArtist) {
        ConfetiArtist confetiArtist = concertArtist.getArtist();

        return new ConcertArtistDTO(
                confetiArtist.getArtistId(),
                confetiArtist.getName(),
                confetiArtist.getProfileUrl(),
                confetiArtist.getLatestReleaseAt()
        );
    }
}

package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import org.sopt.confeti.domain.festival_artist.FestivalArtist;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;

public record FestivalDetailArtistDTO(
        String artistId,
        String name,
        String profileUrl,
        LocalDate latestReleaseAt
) {
    public static FestivalDetailArtistDTO from(final FestivalArtist festivalArtist) {
        ConfetiArtist confetiArtist = festivalArtist.getArtist();
        return new FestivalDetailArtistDTO(
                confetiArtist.getId(),
                confetiArtist.getName(),
                confetiArtist.getProfileUrl(),
                confetiArtist.getLatestReleaseAlbum().getReleaseAt()
        );
    }
}

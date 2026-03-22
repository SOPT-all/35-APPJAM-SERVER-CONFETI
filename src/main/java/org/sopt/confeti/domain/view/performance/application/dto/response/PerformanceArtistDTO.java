package org.sopt.confeti.domain.view.performance.application.dto.response;

import org.sopt.confeti.domain.view.performance.PerformanceArtist;

public record PerformanceArtistDTO(
    Long id,
    String artistId
) {

    public static PerformanceArtistDTO from(PerformanceArtist artist) {
        return new PerformanceArtistDTO(
            artist.getId(),
            artist.getArtistId()
        );
    }
}

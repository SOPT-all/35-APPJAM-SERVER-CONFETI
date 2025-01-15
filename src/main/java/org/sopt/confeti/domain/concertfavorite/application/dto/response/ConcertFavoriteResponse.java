package org.sopt.confeti.domain.concertfavorite.application.dto.response;

import lombok.Builder;
import java.time.LocalDateTime;

@Builder
public class ConcertFavoriteResponse(
        long performanceId,
        String type,
        String subtitle,
        LocalDateTime reserveAt,
        String reservationBgUrl
) {
    public static ConcertFavoriteResponse of(
            final long performanceId,
            final String type,
            final String subtitle,
            final LocalDateTime reserveAt,
            final String reservationBgUrl){
        return ConcertFavoriteResponse.builder()
                .performanceId(performanceId)
                .type(type)
                .subtitle(subtitle)
                .reserveAt(reserveAt)
                .reservationBgUrl(reservationBgUrl)
                .build();
    }
}


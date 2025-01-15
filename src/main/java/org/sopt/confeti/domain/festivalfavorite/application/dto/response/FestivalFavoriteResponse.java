package org.sopt.confeti.domain.festivalfavorite.application.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FestivalFavoriteResponse (
        long performanceId,
        String type,
        String subtitle,
        LocalDateTime reserveAt,
        String reservationBgUrl
){
    public static FestivalFavoriteResponse of(
            final long performanceId,
            final String type,
            final String subtitle,
            final LocalDateTime reserveAt,
            final String reservationBgUrl){
        return FestivalFavoriteResponse.builder()
                .performanceId(performanceId)
                .type(type)
                .subtitle(subtitle)
                .reserveAt(reserveAt)
                .reservationBgUrl(reservationBgUrl)
                .build();
    }
}

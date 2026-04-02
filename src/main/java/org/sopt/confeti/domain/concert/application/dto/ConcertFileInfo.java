package org.sopt.confeti.domain.concert.application.dto;

import lombok.Builder;

@Builder
public record ConcertFileInfo(
    String posterUrl
) {

}

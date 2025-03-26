package org.sopt.confeti.domain.concert.application.dto.request;

public record ConcertFileNamesDTO(
        String poster,
        String posterBg,
        String infoImg
) {
    public static ConcertFileNamesDTO of(
            final String poster, final String posterBg, final String infoImg
    ) {
        return new ConcertFileNamesDTO(poster, posterBg, infoImg);
    }
}

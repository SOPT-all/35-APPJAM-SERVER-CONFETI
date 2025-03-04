package org.sopt.confeti.domain.concert.application.dto.request;

public record ConcertFileNamesDTO(
        String poster,
        String posterBg,
        String infoImg,
        String reservationBg
) {
    public static ConcertFileNamesDTO of(
            final String poster, final String posterBg, final String infoImg, final String reservationBg
    ) {
        return new ConcertFileNamesDTO(poster, posterBg, infoImg, reservationBg);
    }
}

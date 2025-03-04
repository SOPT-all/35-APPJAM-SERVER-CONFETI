package org.sopt.confeti.domain.festival.application.dto.request;

public record FestivalFileNamesDTO(
        String poster,
        String posterBg,
        String infoImg,
        String reservationBg,
        String logo
) {
    public static FestivalFileNamesDTO of(
            final String poster, final String posterBg, final String infoImg, final String reservationBg, final String logo
    ) {
        return new FestivalFileNamesDTO(poster, posterBg, infoImg, reservationBg, logo);
    }
}

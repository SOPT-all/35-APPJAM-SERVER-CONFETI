package org.sopt.confeti.api.performance.facade.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.api.performance.dto.request.CreateConcertRequest;
import org.sopt.confeti.global.util.DateConvertor;

public record CreateConcertDTO(
        String concertTitle,
        String concertSubtitle,
        LocalDateTime concertStartAt,
        LocalDateTime concertEndAt,
        String concertArea,
        String concertPosterPath,
        String concertPosterBgPath,
        String concertInfoImgPath,
        String concertReservationBgPath,
        LocalDateTime reserveAt,
        String reservationUrl,
        String reservationOffice,
        String ageRating,
        String time,
        String price,
        List<CreateConcertArtistDTO> concertArtists
) {
    public static CreateConcertDTO from(final CreateConcertRequest createConcertRequest) {
        return new CreateConcertDTO(
                createConcertRequest.concertTitle(),
                createConcertRequest.concertSubtitle(),
                createConcertRequest.concertStartAt().atStartOfDay(),
                createConcertRequest.concertEndAt().atStartOfDay(),
                createConcertRequest.concertArea(),
                createConcertRequest.concertPosterPath(),
                createConcertRequest.concertPosterBgPath(),
                createConcertRequest.concertInfoImgPath(),
                createConcertRequest.concertReservationBgPath(),
                createConcertRequest.reserveAt().atStartOfDay(),
                createConcertRequest.reservationUrl(),
                createConcertRequest.reservationOffice(),
                createConcertRequest.ageRating(),
                createConcertRequest.time(),
                createConcertRequest.price(),
                createConcertRequest.concertArtists().stream()
                        .map(CreateConcertArtistDTO::from)
                        .toList()
        );
    }
}

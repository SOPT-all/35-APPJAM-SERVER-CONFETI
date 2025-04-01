package org.sopt.confeti.api.dummy.facade.dto.concert.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.sopt.confeti.api.dummy.dto.concert.CreateConcertRequest;
import org.sopt.confeti.api.dummy.facade.dto.concert.ConcertFilePathsDTO;

public record CreateConcertDTO(
        String title,
        String subtitle,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String area,
        String posterPath,
        String posterBgPath,
        LocalDateTime reserveAt,
        String ageRating,
        String time,
        String price,
        String address,
        List<CreateConcertArtistDTO> artists,
        List<CreateConcertMusicDTO> musics,
        List<CreateConcertReservationUrlDTO> reservationUrls
) {
    public static CreateConcertDTO of(CreateConcertRequest request, ConcertFilePathsDTO filePaths) {
        AtomicInteger atomicInteger = new AtomicInteger();
        return new CreateConcertDTO(
                request.getTitle(),
                request.getSubtitle(),
                request.getStartAt(),
                request.getEndAt(),
                request.getArea(),
                filePaths.posterPath(),
                filePaths.posterBgPath(),
                request.getReserveAt(),
                request.getAgeRating(),
                request.getTime(),
                request.getPrice(),
                request.getAddress(),
                request.getArtists().stream()
                        .map(CreateConcertArtistDTO::from)
                        .toList(),
                request.getMusics().stream()
                        .map(CreateConcertMusicDTO::from)
                        .toList(),
                request.getReservationUrls().stream()
                        .map(reservationUrl -> CreateConcertReservationUrlDTO.of(reservationUrl,
                                filePaths.reservationLogoPaths().get(
                                        atomicInteger.getAndIncrement())))
                        .toList()
        );
    }
}

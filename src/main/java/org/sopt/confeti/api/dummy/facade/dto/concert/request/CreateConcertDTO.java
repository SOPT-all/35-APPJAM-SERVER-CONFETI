package org.sopt.confeti.api.dummy.facade.dto.concert.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.sopt.confeti.api.dummy.dto.concert.CreateConcertRequest;
import org.sopt.confeti.api.dummy.facade.dto.concert.ConcertFilePathsDTO;

public record CreateConcertDTO(
        String title,
        String subtitle,
        LocalDate startAt,
        LocalDate endAt,
        String area,
        String posterPath,
        LocalDateTime reserveAt,
        String ageRating,
        String time,
        String price,
        String address,
        List<CreateConcertArtistDTO> artists,
        List<CreateConcertReservationUrlDTO> reservationUrls
) {
    public static CreateConcertDTO of(CreateConcertRequest request, ConcertFilePathsDTO filePaths) {
        AtomicInteger atomicInteger = new AtomicInteger();
        return new CreateConcertDTO(
                request.getTitle(),
                request.getSubtitle(),
                request.getStartAt().toLocalDate(),
                request.getEndAt().toLocalDate(),
                request.getArea(),
                filePaths.posterPath(),
                request.getReserveAt(),
                request.getAgeRating(),
                request.getTime(),
                request.getPrice(),
                request.getAddress(),
                request.getArtists().stream()
                        .map(CreateConcertArtistDTO::from)
                        .toList(),
                request.getReservationUrls().stream()
                        .map(reservationUrl -> CreateConcertReservationUrlDTO.of(reservationUrl,
                                filePaths.reservationLogoPaths().get(
                                        atomicInteger.getAndIncrement())))
                        .toList()
        );
    }
}

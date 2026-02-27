package org.sopt.confeti.api.dummy.facade.dto.concert.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.sopt.confeti.api.dummy.dto.concert.CreateConcertRequest;
import org.sopt.confeti.api.dummy.facade.dto.concert.ConcertFilePathsDTO;
import org.sopt.confeti.domain.ticketvendor.TicketVendor;

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
    public static CreateConcertDTO from(CreateConcertRequest request, ConcertFilePathsDTO filePaths) {
        return new CreateConcertDTO(
                request.title(),
                request.subtitle(),
                request.startAt().toLocalDate(),
                request.endAt().toLocalDate(),
                request.area(),
                filePaths.posterPath(),
                request.reserveAt(),
                request.ageRating(),
                request.time(),
                request.price(),
                request.address(),
                request.artists().stream()
                        .map(CreateConcertArtistDTO::from)
                        .toList(),
                java.util.stream.IntStream.range(0, request.reservationUrls().size())
                        .mapToObj(i -> CreateConcertReservationUrlDTO.from(
                                request.reservationUrls().get(i),
                                filePaths.reservationLogoPaths().get(i).logoPath()
                        ))
                        .toList()
        );
    }
}

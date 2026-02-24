package org.sopt.confeti.api.dummy.facade.dto.festival.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.sopt.confeti.api.dummy.dto.festival.CreateFestivalRequest;
import org.sopt.confeti.api.dummy.facade.dto.festival.FestivalFilePathsDTO;
import org.sopt.confeti.domain.festival.infra.TimetableSupportStatus;
import org.sopt.confeti.domain.ticketvendor.TicketVendor;

public record CreateFestivalDTO(
        String title,
        String subtitle,
        LocalDate startAt,
        LocalDate endAt,
        String area,
        String posterPath,
        String logoPath,
        LocalDateTime reserveAt,
        String ageRating,
        String time,
        String price,
        String address,
        TimetableSupportStatus timetableSupportStatus,
        List<CreateFestivalReservationUrlDTO> reservationUrls,
        List<CreateFestivalDateDTO> dates
) {
    public static CreateFestivalDTO from(CreateFestivalRequest request, FestivalFilePathsDTO filePaths) {
        return new CreateFestivalDTO(
                request.title(),
                request.subtitle(),
                request.startAt(),
                request.endAt(),
                request.area(),
                filePaths.posterPath(),
                filePaths.logoPath(),
                request.reserveAt(),
                request.ageRating(),
                request.time(),
                request.price(),
                request.address(),
                request.timetableSupportStatus(),
                java.util.stream.IntStream.range(0, request.reservationUrls().size())
                        .mapToObj(i -> CreateFestivalReservationUrlDTO.from(
                                request.reservationUrls().get(i),
                                filePaths.reservationLogoPaths().get(i).logoPath()
                        ))
                        .toList(),
                request.dates().stream()
                        .map(CreateFestivalDateDTO::from)
                        .toList()
        );
    }
}

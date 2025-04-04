package org.sopt.confeti.api.dummy.facade.dto.festival.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.sopt.confeti.api.dummy.dto.festival.CreateFestivalRequest;
import org.sopt.confeti.api.dummy.facade.dto.festival.FestivalFilePathsDTO;

public record CreateFestivalDTO(
        String title,
        String subtitle,
        LocalDate startAt,
        LocalDate endAt,
        String area,
        String posterPath,
        String posterBgPath,
        String logoPath,
        LocalDateTime reserveAt,
        String ageRating,
        String time,
        String price,
        String address,
        List<CreateFestivalMusicDTO> musics,
        List<CreateFestivalReservationUrlDTO> reservationUrls,
        List<CreateFestivalDateDTO> dates
) {
    public static CreateFestivalDTO of(CreateFestivalRequest request, FestivalFilePathsDTO filePaths) {
        AtomicInteger atomicInteger = new AtomicInteger();
        return new CreateFestivalDTO(
                request.getTitle(),
                request.getSubtitle(),
                request.getStartAt().toLocalDate(),
                request.getEndAt().toLocalDate(),
                request.getArea(),
                filePaths.posterPath(),
                filePaths.posterBgPath(),
                filePaths.logoPath(),
                request.getReserveAt(),
                request.getAgeRating(),
                request.getTime(),
                request.getPrice(),
                request.getAddress(),
                request.getMusics().stream()
                        .map(CreateFestivalMusicDTO::from)
                        .toList(),
                request.getReservationUrls().stream()
                        .map(reservationUrl -> CreateFestivalReservationUrlDTO.of(reservationUrl,
                                filePaths.reservationLogoPaths().get(
                                        atomicInteger.getAndIncrement())))
                        .toList(),
                request.getDates().stream()
                        .map(CreateFestivalDateDTO::from)
                        .toList()
        );
    }
}

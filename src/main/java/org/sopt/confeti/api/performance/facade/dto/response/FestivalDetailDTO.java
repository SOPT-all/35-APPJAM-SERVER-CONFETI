package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.global.util.S3FileHandler;

public record FestivalDetailDTO(
        long festivalId,
        String festivalTitle,
        String festivalSubtitle,
        LocalDate festivalStartAt,
        LocalDate festivalEndAt,
        String festivalArea,
        String festivalPosterUrl,
        String festivalPosterBgUrl,
        String festivalInfoImgUrl,
        String festivalReservationBgUrl,
        String festivalLogoUrl,
        LocalDateTime reserveAt,
        String reservationUrl,
        String reservationOffice,
        String ageRating,
        String time,
        String price,
        boolean isFavorite,
        List<FestivalDetailDateDTO> dates
) {
    public static FestivalDetailDTO of(final Festival festival, boolean isFavorite, final S3FileHandler s3FileHandler) {
        return new FestivalDetailDTO(
                festival.getId(),
                festival.getFestivalTitle(),
                festival.getFestivalSubtitle(),
                festival.getFestivalStartAt(),
                festival.getFestivalEndAt(),
                festival.getFestivalArea(),
                s3FileHandler.getFileUrl(festival.getFestivalPosterPath()),
                s3FileHandler.getFileUrl(festival.getFestivalPosterBgPath()),
                s3FileHandler.getFileUrl(festival.getFestivalInfoImgPath()),
                s3FileHandler.getFileUrl(festival.getFestivalReservationBgPath()),
                s3FileHandler.getFileUrl(festival.getFestivalLogoPath()),
                festival.getReserveAt(),
                festival.getReservationUrl(),
                festival.getReservationOffice(),
                festival.getAgeRating(),
                festival.getTime(),
                festival.getPrice(),
                isFavorite,
                festival.getDates().stream()
                        .map(FestivalDetailDateDTO::from)
                        .toList()
        );
    }
}

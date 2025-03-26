package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record FestivalDetailDTO(
        long festivalId,
        String festivalTitle,
        String festivalSubtitle,
        LocalDateTime festivalStartAt,
        LocalDateTime festivalEndAt,
        String festivalArea,
        String festivalPosterUrl,
        String festivalPosterBgUrl,
        String festivalInfoImgUrl,
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
                festival.getTitle(),
                festival.getSubtitle(),
                festival.getStartAt(),
                festival.getEndAt(),
                festival.getArea(),
                s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER), festival.getPosterPath()).toString(),
                s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER_BG), festival.getPosterBgPath()).toString(),
                s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.DETAIL), festival.getFestivalInfoImgPath()).toString(),
                s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.LOGO), festival.getLogoPath()).toString(),
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

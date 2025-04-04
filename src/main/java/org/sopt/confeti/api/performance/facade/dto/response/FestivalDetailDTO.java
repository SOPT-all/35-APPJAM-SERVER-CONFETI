package org.sopt.confeti.api.performance.facade.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record FestivalDetailDTO(
        long festivalId,
        String title,
        String subtitle,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String area,
        String posterUrl,
        String posterBgUrl,
        String logoUrl,
        LocalDateTime reserveAt,
        String ageRating,
        String time,
        String price,
        boolean isFavorite,
        String address,
        List<FestivalReservationDTO> reservations,
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
                s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER),
                        festival.getPosterPath()).toString(),
                s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.POSTER_BG),
                        festival.getPosterBgPath()).toString(),
                s3FileHandler.getFileUrl(FolderPath.combine(FolderPath.FESTIVAL, FolderPath.LOGO),
                        festival.getLogoPath()).toString(),
                festival.getReserveAt(),
                festival.getAgeRating(),
                festival.getTime(),
                festival.getPrice(),
                isFavorite,
                festival.getAddress(),
                festival.getReservationUrls().stream()
                        .map(reservation -> FestivalReservationDTO.of(reservation, s3FileHandler))
                        .toList(),
                festival.getDates().stream()
                        .map(FestivalDetailDateDTO::from)
                        .toList()
        );
    }
}

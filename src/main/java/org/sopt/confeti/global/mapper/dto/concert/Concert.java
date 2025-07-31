package org.sopt.confeti.global.mapper.dto.concert;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record Concert(
        long performanceId,
        String title,
        String subtitle,
        LocalDate startAt,
        LocalDate endAt,
        String area,
        String posterUrl,
        LocalDateTime reserveAt,
        String ageRating,
        String time,
        String price,
        String address,
        List<ConcertReservation> reservations,
        List<ConcertArtist> artists
) {
    public static Concert toConcert(Performance performance, S3FileHandler s3FileHandler) {
        return new Concert(
                performance.getId(),
                performance.getTitle(),
                performance.getSubtitle(),
                performance.getStartAt(),
                performance.getEndAt(),
                performance.getArea(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.getFolderPathByPerformanceType(PerformanceType.CONCERT), FolderPath.POSTER),
                        performance.getPosterPath()
                ).toString(),
                performance.getReserveAt(),
                performance.getAgeRating(),
                performance.getTime(),
                performance.getPrice(),
                performance.getAddress(),
                performance.getReservationUrls().stream()
                        .map(reservationUrl -> ConcertReservation.of(reservationUrl, s3FileHandler))
                        .toList(),
                performance.getSchedules().stream()
                        .map(ConcertArtist::from)
                        .toList()
        );
    }
}

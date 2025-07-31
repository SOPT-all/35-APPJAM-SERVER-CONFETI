package org.sopt.confeti.global.mapper.dto.festival;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.domain.performance_schedule.PerformanceSchedule;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

public record Festival(
        long performanceId,
        String title,
        String subtitle,
        LocalDate startAt,
        LocalDate endAt,
        String area,
        String posterUrl,
        String logoUrl,
        LocalDateTime reserveAt,
        String ageRating,
        String time,
        String price,
        String address,
        List<FestivalReservation> reservations,
        List<FestivalDate> dates
) {
    public static Festival toFestival(Performance performance, S3FileHandler s3FileHandler) {
        Map<LocalDate, List<PerformanceSchedule>> scheduleMap = performance.getSchedules().stream()
                .collect(Collectors.groupingBy(PerformanceSchedule::getPerformanceAt));

        return new Festival(
                performance.getId(),
                performance.getTitle(),
                performance.getSubtitle(),
                performance.getStartAt(),
                performance.getEndAt(),
                performance.getArea(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.getFolderPathByPerformanceType(PerformanceType.FESTIVAL), FolderPath.POSTER),
                        performance.getPosterPath()
                ).toString(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.getFolderPathByPerformanceType(PerformanceType.FESTIVAL), FolderPath.LOGO),
                        performance.getLogoPath()
                ).toString(),
                performance.getReserveAt(),
                performance.getAgeRating(),
                performance.getTime(),
                performance.getPrice(),
                performance.getAddress(),
                performance.getReservationUrls().stream()
                        .map(reservationUrl -> FestivalReservation.of(reservationUrl, s3FileHandler))
                        .toList(),
                scheduleMap.keySet().stream()
                        .map(performanceDate -> FestivalDate.of(performanceDate, scheduleMap.get(performanceDate)))
                        .toList()
        );
    }
}

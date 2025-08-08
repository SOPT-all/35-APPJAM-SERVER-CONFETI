package org.sopt.confeti.domain.performance;

import org.sopt.confeti.domain.elastic_search.application.dto.response.SearchPerformanceResult;
import org.sopt.confeti.domain.performance_schedule.SearchedPerformanceSchedule;
import org.sopt.confeti.global.common.constant.FolderPath;
import org.sopt.confeti.global.util.S3FileHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record SearchedPerformance(
        Long id,
        PerformanceType type,
        String title,
        String subtitle,
        String area,
        String address,
        LocalDate startAt,
        LocalDate endAt,
        String ageRating,
        String time,
        String price,
        LocalDateTime reserveAt,
        String posterUrl,
        String logoUrl,
        List<SearchedPerformanceSchedule> schedules
) {
    public static SearchedPerformance of(Performance performance, S3FileHandler s3FileHandler) {
        return new SearchedPerformance(
                performance.getId(),
                performance.getType(),
                performance.getTitle(),
                performance.getSubtitle(),
                performance.getArea(),
                performance.getAddress(),
                performance.getStartAt(),
                performance.getEndAt(),
                performance.getAgeRating(),
                performance.getTime(),
                performance.getPrice(),
                performance.getReserveAt(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.getFolderPathByPerformanceType(performance.getType()), FolderPath.POSTER),
                        performance.getPosterPath()
                ).toString(),
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.getFolderPathByPerformanceType(performance.getType()), FolderPath.POSTER),
                        performance.getLogoPath()
                ).toString(),
                performance.getSchedules().stream()
                        .map(SearchedPerformanceSchedule::from)
                        .toList()
        );
    }

    public static SearchedPerformance of(SearchPerformanceResult searchResult, S3FileHandler s3FileHandler) {
        return new SearchedPerformance(
                searchResult.id(),
                searchResult.type(),
                searchResult.title(),
                null,
                searchResult.area(),
                null,
                searchResult.startAt(),
                searchResult.endAt(),
                null,
                null,
                null,
                null,
                s3FileHandler.getFileUrl(
                        FolderPath.combine(FolderPath.getFolderPathByPerformanceType(searchResult.type()), FolderPath.POSTER),
                        searchResult.posterPath()
                ).toString(),
                null,
                List.of()
        );
    }
}

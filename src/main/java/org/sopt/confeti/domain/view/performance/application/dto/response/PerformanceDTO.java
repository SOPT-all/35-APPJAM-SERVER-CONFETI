package org.sopt.confeti.domain.view.performance.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.sopt.confeti.domain.elastic_search.application.dto.response.SearchPerformanceResult;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.global.common.constant.PerformanceType;

public record PerformanceDTO(
        long id,
        Long typeId,
        PerformanceType type,
        String area,
        String title,
        LocalDate startAt,
        LocalDate endAt,
        String posterPath,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<PerformanceArtistDTO> artists
) {
    public static PerformanceDTO from(final Performance performance) {
        return new PerformanceDTO(
                performance.getId(),
                performance.getTypeId(),
                performance.getType(),
                performance.getArea(),
                performance.getTitle(),
                performance.getStartAt(),
                performance.getEndAt(),
                performance.getPosterPath(),
                performance.getCreatedAt(),
                performance.getUpdatedAt(),
                performance.getArtists().stream()
                        .map(PerformanceArtistDTO::from)
                        .toList()
        );
    }

    public static PerformanceDTO from(final SearchPerformanceResult searchedPerformance) {
        return new PerformanceDTO(
                searchedPerformance.id(),
                searchedPerformance.typeId(),
                searchedPerformance.type(),
                searchedPerformance.area(),
                searchedPerformance.title(),
                searchedPerformance.startAt(),
                searchedPerformance.endAt(),
                searchedPerformance.posterPath(),
                null,
                null,
                List.of()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PerformanceDTO that = (PerformanceDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

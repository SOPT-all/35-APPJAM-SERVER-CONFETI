package org.sopt.confeti.domain.view.performance.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import org.sopt.confeti.domain.elastic_search.application.dto.response.SearchPerformanceResult;
import org.sopt.confeti.domain.view.performance.Performance_DPRECATED;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;

public record PerformanceDTO(
        long id,
        Long typeId,
        PerformanceType_DEPRECATED type,
        String area,
        String title,
        String subtitle,
        LocalDate startAt,
        LocalDate endAt,
        String posterPath,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PerformanceDTO from(final Performance_DPRECATED performanceDPRECATED) {
        return new PerformanceDTO(
                performanceDPRECATED.getId(),
                performanceDPRECATED.getTypeId(),
                performanceDPRECATED.getType(),
                performanceDPRECATED.getArea(),
                performanceDPRECATED.getTitle(),
                performanceDPRECATED.getSubtitle(),
                performanceDPRECATED.getStartAt(),
                performanceDPRECATED.getEndAt(),
                performanceDPRECATED.getPosterPath(),
                performanceDPRECATED.getCreatedAt(),
                performanceDPRECATED.getUpdatedAt()
        );
    }

    public static PerformanceDTO from(final SearchPerformanceResult searchedPerformance) {
        return new PerformanceDTO(
                searchedPerformance.id(),
                searchedPerformance.typeId(),
                searchedPerformance.type(),
                searchedPerformance.area(),
                searchedPerformance.title(),
                null,
                searchedPerformance.startAt(),
                searchedPerformance.endAt(),
                searchedPerformance.posterPath(),
                null,
                null
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

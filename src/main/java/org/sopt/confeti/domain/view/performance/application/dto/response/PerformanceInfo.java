package org.sopt.confeti.domain.view.performance.application.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.Builder;
import org.sopt.confeti.domain.elastic_search.application.dto.response.SearchPerformanceResult;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.domain.view.performance.PerformanceFileInfo;
import org.sopt.confeti.global.common.constant.PerformanceType;

@Builder
public record PerformanceInfo(
    long id,
    Long typeId,
    PerformanceType type,
    String area,
    String title,
    LocalDate startAt,
    LocalDate endAt,
    String posterUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<PerformanceArtistDTO> artists
) {

    public static PerformanceInfo of(Performance performance, PerformanceFileInfo fileInfo) {
        return PerformanceInfo.builder()
            .id(performance.getId())
            .typeId(performance.getTypeId())
            .type(performance.getType())
            .area(performance.getArea())
            .title(performance.getTitle())
            .startAt(performance.getStartAt())
            .endAt(performance.getEndAt())
            .posterUrl(fileInfo.posterUrl())
            .createdAt(performance.getCreatedAt())
            .updatedAt(performance.getUpdatedAt())
            .artists(performance.getArtists().stream()
                .map(PerformanceArtistDTO::from)
                .toList())
            .build();
    }

    public static PerformanceInfo of(SearchPerformanceResult result, PerformanceFileInfo fileInfo) {
        return PerformanceInfo.builder()
            .id(result.id())
            .typeId(result.typeId())
            .type(result.type())
            .area(result.area())
            .title(result.title())
            .startAt(result.startAt())
            .endAt(result.endAt())
            .posterUrl(fileInfo.posterUrl())
            .artists(List.of())
            .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PerformanceInfo that = (PerformanceInfo) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

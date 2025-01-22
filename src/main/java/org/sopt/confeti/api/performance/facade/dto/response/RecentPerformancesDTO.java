package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.festival.Festival;

public record RecentPerformancesDTO(
        List<RecentPerformanceDTO> performances
) {
    public static RecentPerformancesDTO of(
            final List<Concert> concerts, final Map<Long, Long> concertMapper,
            final List<Festival> festivals, final Map<Long, Long> festivalMapper,
            final int size
    ) {
        return new RecentPerformancesDTO(
                Stream.concat(
                        concerts.stream()
                                .map(concert -> RecentPerformanceDTO.of(concert, concertMapper.get(concert.getId()))),
                        festivals.stream()
                                .map(festival -> RecentPerformanceDTO.of(festival, festivalMapper.get(festival.getId())))
                )
                        .sorted(Comparator.comparing(RecentPerformanceDTO::performanceAt))
                        .limit(size)
                        .toList()
        );
    }
}

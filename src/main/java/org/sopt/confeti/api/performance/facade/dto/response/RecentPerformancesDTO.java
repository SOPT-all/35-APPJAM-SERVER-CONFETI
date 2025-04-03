package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.view.performance.Performance;

public record RecentPerformancesDTO(
        boolean isPersonalized,
        List<RecentPerformanceDTO> performances
) {
    public static RecentPerformancesDTO of(
            boolean isPersonalized,
            List<Concert> concerts,
            List<Festival> festivals,
            int recentPerformanceSize
    ) {
        List<RecentPerformanceDTO> performances = Stream.concat(
                IntStream.range(0, concerts.size())
                        .mapToObj(i -> RecentPerformanceDTO.of(concerts.get(i), i)),
                IntStream.range(0, festivals.size())
                        .mapToObj(i -> RecentPerformanceDTO.of(festivals.get(i), i + concerts.size()))
        ).toList();

        if (performances.size() >= recentPerformanceSize) {
            performances = performances.subList(0, recentPerformanceSize);
        }

        return new RecentPerformancesDTO(isPersonalized, performances);
    }

    public static RecentPerformancesDTO from(boolean isPersonalized, List<Performance> performances) {
        return new RecentPerformancesDTO(
                isPersonalized,
                performances.stream()
                        .map(RecentPerformanceDTO::from)
                        .toList()
        );
    }
}

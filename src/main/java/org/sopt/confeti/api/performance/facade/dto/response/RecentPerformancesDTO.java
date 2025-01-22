package org.sopt.confeti.api.performance.facade.dto.response;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.view.performance.Performance;

public record RecentPerformancesDTO(
        List<RecentPerformanceDTO> performances
) {
    public static RecentPerformancesDTO of(
            List<Concert> concerts,
            List<Festival> festivals
    ) {
        return new RecentPerformancesDTO(
                Stream.concat(
                        IntStream.range(0, concerts.size())
                                .mapToObj(i -> RecentPerformanceDTO.of(concerts.get(i), i)),
                        IntStream.range(concerts.size(), festivals.size())
                                .mapToObj(i -> RecentPerformanceDTO.of(festivals.get(i), i))
                ).toList()
        );
    }

    public static RecentPerformancesDTO from(final List<Performance> performances) {
        return new RecentPerformancesDTO(
                performances.stream()
                        .map(RecentPerformanceDTO::from)
                        .toList()
        );
    }
}

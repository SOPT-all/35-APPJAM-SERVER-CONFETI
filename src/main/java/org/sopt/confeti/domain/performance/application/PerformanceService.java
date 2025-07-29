package org.sopt.confeti.domain.performance.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.performance.infra.repository.PerformanceRepository;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformancePreviewDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private static final int INIT_PAGE = 0;
    private static final String CREATED_AT_COLUMN = "createdAt";
    private static final int FAVORITE_PERFORMANCE_PREVIEW_COUNT = 4;

    private final PerformanceRepository performanceRepository;

    @Transactional(readOnly = true)
    public Optional<Performance> getPerformance(long performanceId) {
        return performanceRepository.findById(performanceId);
    }

    @Transactional(readOnly = true)
    public List<Performance> getRecentPerformances(int fetchSize) {
        return performanceRepository.findRecentPerformances(
                LocalDate.now(),
                getPageRequest(fetchSize, getRecentPerformancesSort())
        ).stream().toList();
    }

    @Transactional(readOnly = true)
    public List<Performance> getRecentPerformancesWithFavoriteArtists(List<String> artistIds, int fetchSize) {
        return performanceRepository.findRecentPerformancesByArtistIds(
                artistIds,
                getPageRequest(fetchSize, getRecentPerformancesSort())
        );
    }

    @Transactional(readOnly = true)
    public List<Performance> getFavoriteRecentPerformances(long userId) {
        return performanceRepository.findFavoriteRecentPerformances(
                userId,
                getPageRequest(FAVORITE_PERFORMANCE_PREVIEW_COUNT, getRecentPerformancesSort())
        );
    }

    private PageRequest getPageRequest(final int size, final Sort sort) {
        return PageRequest.of(INIT_PAGE, size, sort);
    }

    private Sort getRecentPerformancesSort() {
        return Sort.by(
                Order.desc(CREATED_AT_COLUMN)
        );
    }
}

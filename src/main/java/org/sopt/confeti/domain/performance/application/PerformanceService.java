package org.sopt.confeti.domain.performance.application;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.performance.infra.repository.PerformanceRepository;
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
    private static final String TITLE_COLUMN = "title";

    private final PerformanceRepository performanceRepository;

    @Transactional(readOnly = true)
    public Optional<Performance> getPerformance(long performanceId) {
        return performanceRepository.findById(performanceId);
    }

    @Transactional(readOnly = true)
    public Optional<Performance> getExpectedPerformance(long performanceId) {
        return performanceRepository.findById(performanceId, getRecentPerformancesSort());
    }

    @Transactional(readOnly = true)
    public List<Performance> getExpectedPerformances(int fetchSize) {
        return performanceRepository.findExpectedPerformances(
                getPageRequest(fetchSize, getRecentPerformancesSort())
        ).stream().toList();
    }

    @Transactional(readOnly = true)
    public List<Performance> getExpectedPerformancesIn(List<Long> performanceIds) {
        return performanceRepository.findExpectedPerformancesByIdIn(performanceIds);
    }

    @Transactional(readOnly = true)
    public List<Performance> getExpectedPerformancesWithFavoriteArtists(List<String> artistIds, int fetchSize) {
        return performanceRepository.findExpectedPerformancesByArtistIds(
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

    @Transactional(readOnly = true)
    public List<Performance> getPerformancesByIds(List<Long> performanceIds) {
        return performanceRepository.findAllById(performanceIds);
    }

    @Transactional(readOnly = true)
    public boolean existsById(long performanceId) {
        return performanceRepository.existsById(performanceId);
    }

    @Transactional(readOnly = true)
    public List<Performance> getRecentPerformancesToAddTimetable(long userId, int size, List<Long> excludePerformanceIds) {
        return performanceRepository.findExpectedPerformancesToAddTimetable(userId, excludePerformanceIds, getPageRequest(size, getFestivalSort()));
    }

    @Transactional(readOnly = true)
    public List<Performance> getRecentPerformancesByArtistId(String aid) {
        return performanceRepository.findExpectedPerformancesByArtistId(aid);
    }

    @Transactional(readOnly = true)
    public List<Performance> getPerformancesByArtistId(String aid) {
        return performanceRepository.findPerformancesBySchedules_ArtistId(aid);
    }

    private PageRequest getPageRequest(final int size, final Sort sort) {
        return PageRequest.of(INIT_PAGE, size, sort);
    }

    private Sort getFestivalSort() {
        return Sort.by(
                Order.asc(TITLE_COLUMN)
        );
    }

    private Sort getRecentPerformancesSort() {
        return Sort.by(
                Order.desc(CREATED_AT_COLUMN)
        );
    }
}

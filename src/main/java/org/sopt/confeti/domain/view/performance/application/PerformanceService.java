package org.sopt.confeti.domain.view.performance.application;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.domain.view.performance.PerformanceArtist;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformancePreviewDTO;
import org.sopt.confeti.domain.view.performance.infra.repository.PerformanceDTORepository;
import org.sopt.confeti.domain.view.performance.infra.repository.PerformanceRepository;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
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

    private final PerformanceDTORepository performanceDTORepository;
    private final PerformanceRepository performanceRepository;

    @Transactional(readOnly = true)
    public List<PerformancePreviewDTO> getFavoritePerformancesPreview(final long userId) {
        return performanceDTORepository.findFavoritePerformancesPreview(userId);
    }

    @Transactional(readOnly = true)
    public List<Performance> getFavoritePerformancesAll(final long userId, final String type) {
        return performanceRepository.findPerformancesByUserFavorites(userId, type);
    }

    @Transactional(readOnly = true)
    public List<PerformanceTicketDTO> getFavoritePerformancesReservation(final Long userId) {
        return performanceDTORepository.findFavoritePerformancesReservation(userId);
    }

    @Transactional(readOnly = true)
    public List<PerformanceTicketDTO> getPerformancesReservation() {
        return performanceDTORepository.findPerformancesReservation();
    }

    @Transactional
    public void create(final Performance performances) {
        performanceRepository.save(performances);
    }

    @Transactional(readOnly = true)
    public List<Performance> getPerformancesByArtistIds(final List<String> artistIds, final int size) {
        return performanceRepository.findPerformancesByArtistIds(
                artistIds,
                getPageRequest(size, getRecentPerformancesSort())
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

    @Transactional(readOnly = true)
    public List<Performance> findPerformanceByArtistId(final String artistId) {
        return performanceRepository.findPerformancesByArtistId(artistId);
    }

    @Transactional(readOnly = true)
    public List<Performance> getRecentPerformances(final int recentPerformancesSize) {
        return performanceRepository.findAll(
                getPageRequest(recentPerformancesSize, getRecentPerformancesSort())
        ).stream().toList();
    }

    @Transactional
    public void addPerformanceArtists(PerformanceType performanceType, long festivalId,
                                      List<PerformanceArtist> performanceArtists) {
        Performance performance = performanceRepository.findPerformancesByTypeAndTypeId(performanceType, festivalId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.NOT_FOUND)
                );

        performance.addArtists(performanceArtists);
    }

    @Transactional
    public Performance getUpcomingPerformance(final Long userId){
        return performanceRepository.upcomingPerformance(userId)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<PerformanceDTO> getAllPerformances() {
        return performanceRepository.findAll().stream()
                .map(org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO::from)
                .toList();
    }
}
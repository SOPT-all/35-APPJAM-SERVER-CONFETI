package org.sopt.confeti.domain.view.performance.application;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.view.performance.*;
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
    public List<PerformanceDTO> getFavoritePerformancesPreview(final long userId) {
        return performanceDTORepository.findFavoritePerformancesPreview(userId);
    }

    @Transactional(readOnly = true)
    public List<Performance> getFavoritePerformancesAll(final long userId, final String type) {
        if (type.equalsIgnoreCase("FESTIVAL")) {
            return performanceRepository.findPerformancesByUserFavorites(userId, PerformanceType.FESTIVAL);
        }
        if (type.equalsIgnoreCase("CONCERT")) {
            return performanceRepository.findPerformancesByUserFavorites(userId, PerformanceType.CONCERT);
        }
        // 'ALL' 경우, 두 가지 데이터를 합침
        List<Performance> allFavorites = new ArrayList<>();
        allFavorites.addAll(performanceRepository.findPerformancesByUserFavorites(userId, PerformanceType.CONCERT));
        allFavorites.addAll(performanceRepository.findPerformancesByUserFavorites(userId, PerformanceType.FESTIVAL));
        allFavorites.sort(Comparator.comparing(Performance::getStartAt));

        return allFavorites;
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
}
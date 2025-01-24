package org.sopt.confeti.domain.view.performance.application;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.domain.view.performance.PerformanceDTO;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;
import org.sopt.confeti.domain.view.performance.application.dto.PerformanceCursorDTO;
import org.sopt.confeti.domain.view.performance.infra.repository.PerformanceDTORepository;
import org.sopt.confeti.domain.view.performance.infra.repository.PerformanceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private static final int INIT_PAGE = 0;
    private static final String START_AT_COLUMN = "performanceStartAt";

    private final PerformanceDTORepository performanceDTORepository;
    private final PerformanceRepository performanceRepository;

    @Transactional(readOnly = true)
    public List<PerformanceDTO> getFavoritePerformancesPreview(final long userId) {
        return performanceDTORepository.findFavoritePerformancesPreview(userId);
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
    public void create(final Festival festival) {
        performanceRepository.saveAll(
                festival.getDates().stream()
                        .flatMap(festivalDate -> festivalDate.getStages().stream())
                        .flatMap(festivalStage -> festivalStage.getTimes().stream())
                        .flatMap(festivalTime -> festivalTime.getArtists().stream()
                                .map(festivalArtist -> Performance.create(festival, festivalArtist.getArtist().getArtistId(), festivalTime.getStartAt()))
                        ).toList()
        );
    }

    @Transactional
    public void create(final Concert concert) {
        performanceRepository.saveAll(
                concert.getArtists().stream()
                        .map(concertArtist -> Performance.create(concert, concertArtist.getArtist().getArtistId()))
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public List<Performance> getPerformancesByArtistIds(final List<String> artistIds, final int size) {
        return performanceRepository.findPerformancesByArtistIdInAndPerformanceEndAtGreaterThanEqual(
                artistIds,
                LocalDateTime.now(),
                getPageRequest(size, getRecentPerformancesSort())
        );
    }

    private PageRequest getPageRequest(final int size, final Sort sort) {
        return PageRequest.of(INIT_PAGE, size, sort);
    }

    private Sort getRecentPerformancesSort() {
        return Sort.by(
                Order.asc(START_AT_COLUMN)
        );
    }

    @Transactional(readOnly = true)
    public List<Performance> findPerformanceUsingInitCursor(final String artistId, final int size) {
        return performanceRepository.findPerformanceUsingInitCursor(artistId, PageRequest.of(INIT_PAGE, size));
    }

    @Transactional(readOnly = true)
    public Optional<PerformanceCursorDTO> findPerformanceCursor(final long performanceId) {
        return performanceRepository.findCursorByPerformanceId(performanceId);
    }

    @Transactional(readOnly = true)
    public List<Performance> getPerformanceUsingCursor(final String artistId, final LocalDateTime performanceCursor, final LocalTime artistPerformanceCursor, final int size){
        return performanceRepository.getPerformanceUsingCursor(artistId, performanceCursor, artistPerformanceCursor, PageRequest.of(INIT_PAGE, size));
    }

}
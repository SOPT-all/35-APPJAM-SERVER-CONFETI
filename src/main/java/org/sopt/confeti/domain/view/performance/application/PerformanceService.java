package org.sopt.confeti.domain.view.performance.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.performance.facade.dto.request.GetUpcomingPerformancesDTO;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.domain.view.performance.PerformanceArtist;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceArtistDTO;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformancePreviewDTO;
import org.sopt.confeti.domain.view.performance.infra.repository.PerformanceCriteriaRepository;
import org.sopt.confeti.domain.view.performance.infra.repository.PerformanceDTORepository;
import org.sopt.confeti.domain.view.performance.infra.repository.PerformanceRepository;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private static final int INIT_PAGE = 0;
    private static final String CREATED_AT_COLUMN = "createdAt";

    private final PerformanceDTORepository performanceDTORepository;
    private final PerformanceRepository performanceRepository;
    private final PerformanceCriteriaRepository performanceCriteriaRepository;

    @Transactional(readOnly = true)
    public List<PerformancePreviewDTO> getFavoritePerformancesPreview(final long userId) {
        return performanceDTORepository.findFavoritePerformancesPreview(userId);
    }

    @Transactional(readOnly = true)
    public List<PerformanceDTO> getFavoritePerformancesAll(final long userId, final String type) {
        return performanceRepository.findPerformancesByUserFavorites(userId, type).stream()
            .map(PerformanceDTO::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<PerformanceTicketDTO> getFavoritePerformancesReservation(final Long userId,
        final int reservationPerformanceCount) {
        return performanceDTORepository.findFavoritePerformancesReservation(userId,
            reservationPerformanceCount);
    }

    @Transactional
    public void create(final Performance performances) {
        performanceRepository.save(performances);
    }

    @Transactional(readOnly = true)
    public List<Performance> getPerformancesByArtistIds(final List<String> artistIds,
        final int size) {
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
    public List<PerformanceDTO> getPerformancesByArtistId(final String artistId) {
        return performanceRepository.findPerformancesByArtistId(artistId).stream()
            .map(PerformanceDTO::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<PerformanceDTO> getAllPerformancesByArtistId(final String artistId) {
        return performanceRepository.findPerformancesByArtists_ArtistId(artistId).stream()
            .map(PerformanceDTO::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<PerformanceTicketDTO> getPerformancesReservationExcluding(
        final List<Long> excludedConcertIds,
        final List<Long> excludedFestivalIds,
        final int limit) {
        return performanceDTORepository.findPerformancesReservationExcluding(
            excludedConcertIds, excludedFestivalIds, limit);
    }

    @Transactional(readOnly = true)
    public List<Performance> getRecentPerformancesExcluding(
        final List<Long> excludedConcertIds,
        final List<Long> excludedFestivalIds,
        final int size) {
        return performanceRepository.findRecentPerformancesExcluding(
            excludedConcertIds,
            excludedFestivalIds,
            LocalDate.now(),
            PerformanceType.CONCERT,
            PerformanceType.FESTIVAL,
            getPageRequest(size, getRecentPerformancesSort())
        );
    }

    @Transactional
    public void addPerformanceArtists(PerformanceType performanceType, long festivalId,
        List<PerformanceArtist> performanceArtists) {
        Performance performance = performanceRepository.findPerformanceByTypeAndTypeId(
                performanceType, festivalId)
            .orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND)
            );

        performance.addArtists(performanceArtists);
    }

    @Transactional
    public Performance getUpcomingPerformanceByUserId(final Long userId) {
        return performanceRepository.upcomingPerformanceByUserId(userId)
            .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<PerformanceDTO> getAllPerformances() {
        return performanceRepository.findAll().stream()
            .map(PerformanceDTO::from)
            .toList();
    }

    @Deprecated
    @Transactional(readOnly = true)
    public List<Performance> getRecommendPerformances() {
        return performanceRepository.findTop5ByRand();
    }

    @Transactional(readOnly = true)
    public List<Performance> getRecommendPerformances(int limit) {
        return performanceRepository.findUpcomingPerformancesByRand(limit);
    }

    @Transactional(readOnly = true)
    public PerformanceDTO getPerformance(long performanceId) {
        Performance performance = performanceRepository.findPerformanceByIdAndEndAtGreaterThanEqual(
                performanceId,
                LocalDate.now())
            .orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND)
            );

        return PerformanceDTO.from(performance);
    }

    @Transactional(readOnly = true)
    public List<PerformanceDTO> getPerformancesByArtistIdAndType(String artistId,
        PerformanceType type) {
        if (type == PerformanceType.PERFORMANCE) {
            return performanceRepository.findPerformancesByArtistId(artistId).stream()
                .map(PerformanceDTO::from)
                .toList();
        }

        return performanceRepository.findPerformancesByTypeAndArtistId(type, artistId).stream()
            .map(PerformanceDTO::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public Optional<Performance> getPerformanceByRand() {
        return performanceRepository.findPerformanceByRand();
    }

    @Transactional(readOnly = true)
    public Optional<Performance> getPerformanceByUserFavorites(final Long userId) {
        return performanceRepository.getPerformanceByUserFavorites(userId);
    }

    @Transactional(readOnly = true)
    public Performance getPerformanceById(long performanceId) {
        return performanceRepository.findById(performanceId)
            .orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND)
            );
    }

    @Transactional(readOnly = true)
    public List<PerformanceDTO> getUpcomingPerformances(
        GetUpcomingPerformancesDTO upcomingPerformancesDTO) {
        List<Pair<PerformanceType, Long>> performancePairs = convertToPairs(
            upcomingPerformancesDTO);
        List<Performance> performances = performanceCriteriaRepository.findPerformancesByTypeAndTypeId(
            performancePairs);

        Map<Pair<PerformanceType, Long>, Performance> performanceMapper = performances.stream()
            .collect(Collectors.toMap(
                performance -> Pair.of(performance.getType(), performance.getTypeId()),
                Function.identity()
            ));

        return performancePairs.stream()
            .map(performanceMapper::get)
            .filter(Objects::nonNull)
            .map(PerformanceDTO::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public Performance getPerformanceByTypeAndTypeId(PerformanceType type, long typeId) {
        return performanceRepository.findPerformanceByTypeAndTypeId(type, typeId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Performance getWithArtistsByTypeAndTypeId(PerformanceType type, long typeId) {
        return performanceRepository.findWithArtistsByTypeAndTypeId(type, typeId)
            .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<PerformanceDTO> getPerformancesByTypeAndTypeIds(PerformanceType type,
        List<Long> typeIds) {
        return performanceRepository.findPerformancesByTypeAndTypeIdIn(type, typeIds).stream()
            .map(PerformanceDTO::from)
            .toList();
    }

    private List<Pair<PerformanceType, Long>> convertToPairs(
        GetUpcomingPerformancesDTO upcomingPerformancesDTO) {
        return upcomingPerformancesDTO.upcomingPerformanceDTOs().stream()
            .map(performanceDTO -> Pair.of(performanceDTO.type(), performanceDTO.typeId()))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<Performance> getPerformances() {
        return performanceRepository.findByEndAtGreaterThanEqual(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public List<PerformanceDTO> getRandomUpcomingPerformances(int fetchSize) {
        return performanceRepository.findUpcomingPerformancesByRand(fetchSize).stream()
            .map(PerformanceDTO::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<PerformanceArtistDTO> getRandomPerformanceArtists(long performanceId,
        int fetchSize) {
        return performanceRepository.findPerformanceArtistsByRand(performanceId, fetchSize).stream()
            .map(PerformanceArtistDTO::from)
            .toList();
    }
}

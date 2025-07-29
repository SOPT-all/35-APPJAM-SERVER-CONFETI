package org.sopt.confeti.domain.view.performance.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.performance.facade.dto.request.GetExpectedPerformancesDTO;
import org.sopt.confeti.domain.view.performance.Performance_DPRECATED;
import org.sopt.confeti.domain.view.performance.PerformanceArtist_DPRECATED;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformancePreviewDTO;
import org.sopt.confeti.domain.view.performance.infra.repository.PerformanceCriteriaRepository_DPRECATED;
import org.sopt.confeti.domain.view.performance.infra.repository.PerformanceDTORepository;
import org.sopt.confeti.domain.view.performance.infra.repository.PerformanceRepository_DPRECATED;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PerformanceService_DPRECATED {

    private static final int INIT_PAGE = 0;
    private static final String CREATED_AT_COLUMN = "createdAt";

    private final PerformanceDTORepository performanceDTORepository;
    private final PerformanceRepository_DPRECATED performanceRepositoryDPRECATED;
    private final PerformanceCriteriaRepository_DPRECATED performanceCriteriaRepositoryDPRECATED;

    @Transactional(readOnly = true)
    public List<PerformancePreviewDTO> getFavoritePerformancesPreview(final long userId) {
        return performanceDTORepository.findFavoritePerformancesPreview(userId);
    }

    @Transactional(readOnly = true)
    public List<PerformanceDTO> getFavoritePerformancesAll(final long userId, final String type) {
        return performanceRepositoryDPRECATED.findPerformancesByUserFavorites(userId, type).stream()
                .map(PerformanceDTO::from)
                .toList();
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
    public void create(final Performance_DPRECATED performances) {
        performanceRepositoryDPRECATED.save(performances);
    }

    @Transactional(readOnly = true)
    public List<Performance_DPRECATED> getPerformancesByArtistIds(final List<String> artistIds, final int size) {
        return performanceRepositoryDPRECATED.findPerformancesByArtistIds(
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
        return performanceRepositoryDPRECATED.findPerformancesByArtistId(artistId).stream()
                .map(PerformanceDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PerformanceDTO> getAllPerformancesByArtistId(final String artistId) {
        return performanceRepositoryDPRECATED.findPerformancesByArtists_ArtistId(artistId).stream()
                .map(PerformanceDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Performance_DPRECATED> getRecentPerformances(final int recentPerformancesSize) {
        return performanceRepositoryDPRECATED.findRecentPerformancesByEndAtGreaterThanEqual(
                LocalDate.now(),
                getPageRequest(recentPerformancesSize, getRecentPerformancesSort())
        ).stream().toList();
    }

    @Transactional
    public void addPerformanceArtists(PerformanceType performanceType, long festivalId,
                                      List<PerformanceArtist_DPRECATED> performanceArtistDPRECATEDS) {
        Performance_DPRECATED performanceDPRECATED = performanceRepositoryDPRECATED.findPerformanceByTypeAndTypeId(performanceType, festivalId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.NOT_FOUND)
                );

        performanceDPRECATED.addArtists(performanceArtistDPRECATEDS);
    }

    @Transactional
    public Performance_DPRECATED getUpcomingPerformanceByUserId(final Long userId) {
        return performanceRepositoryDPRECATED.upcomingPerformanceByUserId(userId)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public List<PerformanceDTO> getAllPerformances() {
        return performanceRepositoryDPRECATED.findAll().stream()
                .map(PerformanceDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Performance_DPRECATED> getRecommendPerformances() {
        return performanceRepositoryDPRECATED.findTop5ByRand();
    }

    @Transactional(readOnly = true)
    public PerformanceDTO getPerformance(long performanceId) {
        Performance_DPRECATED performanceDPRECATED = performanceRepositoryDPRECATED.findPerformanceByIdAndEndAtGreaterThanEqual(
                        performanceId,
                        LocalDate.now())
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.NOT_FOUND)
                );

        return PerformanceDTO.from(performanceDPRECATED);
    }

    @Transactional(readOnly = true)
    public List<PerformanceDTO> getPerformancesByArtistIdAndType(String artistId, PerformanceType type) {
        if (type == PerformanceType.PERFORMANCE) {
            return performanceRepositoryDPRECATED.findPerformancesByArtistId(artistId).stream()
                    .map(PerformanceDTO::from)
                    .toList();
        }

        return performanceRepositoryDPRECATED.findPerformancesByTypeAndArtistId(type, artistId).stream()
                .map(PerformanceDTO::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<Performance_DPRECATED> getPerformanceByRand() {
        return performanceRepositoryDPRECATED.findPerformanceByRand();
    }

    @Transactional(readOnly = true)
    public Optional<Performance_DPRECATED> getPerformanceByUserFavorites(final Long userId) {
        return performanceRepositoryDPRECATED.getPerformanceByUserFavorites(userId);
    }

    @Transactional(readOnly = true)
    public Performance_DPRECATED getPerformanceById(long performanceId) {
        return performanceRepositoryDPRECATED.findById(performanceId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.NOT_FOUND)
                );
    }

    @Transactional(readOnly = true)
    public List<PerformanceDTO> getExpectedPerformances(GetExpectedPerformancesDTO expectedPerformancesDTO) {
        List<Pair<PerformanceType, Long>> performancePairs = convertToPairs(expectedPerformancesDTO);
        List<Performance_DPRECATED> performanceDPRECATEDS = performanceCriteriaRepositoryDPRECATED.findPerformancesByTypeAndTypeId(
                performancePairs);

        Map<Pair<PerformanceType, Long>, Performance_DPRECATED> performanceMapper = performanceDPRECATEDS.stream()
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
    public Performance_DPRECATED getPerformanceByTypeAndTypeId(PerformanceType type, long typeId) {
        return performanceRepositoryDPRECATED.findPerformanceByTypeAndTypeId(type, typeId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
    }

    private List<Pair<PerformanceType, Long>> convertToPairs(GetExpectedPerformancesDTO expectedPerformancesDTO) {
        return expectedPerformancesDTO.expectedPerformanceDTOs().stream()
                .map(performanceDTO -> Pair.of(performanceDTO.type(), performanceDTO.typeId()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Performance_DPRECATED> getPerformances() {
        return performanceRepositoryDPRECATED.findByEndAtGreaterThanEqual(LocalDate.now());
    }
}
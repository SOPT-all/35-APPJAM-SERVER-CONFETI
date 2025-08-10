package org.sopt.confeti.api.performance.facade;

import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.performance.facade.dto.request.GetExpectedPerformanceDTO;
import org.sopt.confeti.api.performance.facade.dto.request.GetExpectedPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.*;
import org.sopt.confeti.domain.artist_favorite.ArtistFavorite;
import org.sopt.confeti.domain.artist_favorite.application.ArtistFavoriteService;
import org.sopt.confeti.domain.concert.application.ConcertService;
import org.sopt.confeti.domain.concert_favorite.application.ConcertFavoriteService;
import org.sopt.confeti.domain.elastic_search.application.PerformanceSearchService;
import org.sopt.confeti.domain.elastic_search.application.SearchTermService;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.festival_favorite.application.FestivalFavoriteService;
import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.performance.application.PerformanceService;
import org.sopt.confeti.domain.performance_favorite.application.PerformanceFavoriteService;
import org.sopt.confeti.domain.setlist.application.SetlistService;
import org.sopt.confeti.domain.timetable_festival.application.TimetableFestivalService;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.domain.view.performance.Performance_DPRECATED;
import org.sopt.confeti.domain.view.performance.PerformanceArtist_DPRECATED;
import org.sopt.confeti.domain.view.performance.application.PerformanceService_DPRECATED;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.common.constant.PerformanceStatus;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.mapper.PerformanceMapper;
import org.sopt.confeti.global.mapper.dto.concert.Concert;
import org.sopt.confeti.global.mapper.dto.festival.Festival;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import org.sopt.confeti.global.util.S3FileHandler;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Facade
@RequiredArgsConstructor
public class PerformanceFacade {

    private static final int RECENT_PERFORMANCES_SIZE = 7;
    private static final int RECOMMEND_MUSIC_SIZE = 3;
    private static final boolean PERSONALIZED = true;
    private static final boolean UNPERSONALIZED = false;

    private final ConcertService concertService;
    private final FestivalService festivalService;
    private final UserService userService;
    private final FestivalFavoriteService festivalFavoriteService;
    private final PerformanceService_DPRECATED performanceServiceDPRECATED;
    private final PerformanceService performanceService;
    private final PerformanceMapper performanceMapper;
    private final ConcertFavoriteService concertFavoriteService;
    private final ArtistFavoriteService artistFavoriteService;
    private final PerformanceSearchService performanceSearchService;
    private final MusicAPIHandler musicAPIHandler;
    private final TimetableFestivalService timetableFestivalService;
    private final SetlistService setlistService;
    private final SearchTermService searchTermService;
    private final PerformanceFavoriteService performanceFavoriteService;
    private final S3FileHandler s3FileHandler;

    @Transactional(readOnly = true)
    public ConcertDetailDTO getConcertDetail(Long userId, long performanceId) {
        boolean isFavorite = getIsFavorite(userId, performanceId);

        Performance performance = performanceService.getExpectedPerformance(performanceId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.NOT_FOUND)
                );
        Concert concert = performanceMapper.toConcert(performance);

        return ConcertDetailDTO.of(concert, isFavorite);
    }

    @Transactional(readOnly = true)
    public FestivalDetailDTO getFestivalDetail(Long userId, long performanceId) {
        boolean isFavorite = getIsFavorite(userId, performanceId);

        Performance performance = performanceService.getExpectedPerformance(performanceId)
                .orElseThrow(
                        () -> new NotFoundException(ErrorMessage.NOT_FOUND)
                );
        Festival festival = performanceMapper.toFestival(performance);

        return FestivalDetailDTO.of(festival, isFavorite);
    }

    protected boolean getIsFavorite(Long userid, long performanceId) {
        if (userid != null) {
            return performanceFavoriteService.isFavorite(userid, performanceId);
        }

        return false;
    }

    @Transactional(readOnly = true)
    public PerformanceReservationsDTO getPerformReservationInfo(Long userId) {
        if (hasUpcomingPerformanceFavorite(userId)) {
            return PerformanceReservationsDTO.from(
                    performanceService.getUpcomingFavoritePerformancesReservation(userId)
            );
        }

        return PerformanceReservationsDTO.from(
                performanceService.getUpcomingPerformancesReservation()
        );
    }

    private boolean hasUpcomingPerformanceFavorite(Long userId) {
        if (userId == null) {
            return false;
        }

        return performanceFavoriteService.hasUpcomingPerformanceFavorite(userId);
    }

    @Transactional(readOnly = true)
    public RecentPerformancesDTO getRecentPerformances(Long userId) {
        if (userId == null || !hasFavoriteArtists(userId)) {
            return getRecentPerformancesWithoutFavorites();
        }

        RecentPerformancesDTO recentPerformances = getRecentPerformancesWithFavoriteArtists(userId);

        if (recentPerformances.performances().isEmpty()) {
            return getRecentPerformancesWithoutFavorites();
        }

        return recentPerformances;
    }

    @Transactional(readOnly = true)
    public RecentPerformancesDTO getRecentPerformancesWithFavoriteArtists(long userId) {
        List<ArtistFavorite> artistFavorites = artistFavoriteService.getArtistIdsByUserId(userId);
        List<String> artistIds = artistFavorites.stream()
                .map(artistFavorite -> artistFavorite.getArtist().getId())
                .toList();

        List<Performance> performances = performanceService.getExpectedPerformancesWithFavoriteArtists(artistIds, RECENT_PERFORMANCES_SIZE);

        return RecentPerformancesDTO.of(PERSONALIZED, performances);
    }

    @Transactional(readOnly = true)
    public RecentPerformancesDTO getRecentPerformancesWithoutFavorites() {
        List<Performance> performances = performanceService.getExpectedPerformances(RECENT_PERFORMANCES_SIZE);
        return RecentPerformancesDTO.of(
                UNPERSONALIZED,
                performances
        );
    }

    @Transactional(readOnly = true)
    public boolean hasFavoriteArtists(long userId) {
        return artistFavoriteService.existsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public ArtistPerformancesDTO getPerformancesByArtistId(Long userId, String artistId) {
        List<PerformanceDTO> performances = performanceServiceDPRECATED.getPerformancesByArtistId(artistId);

        Map<String, Boolean> favoriteMap = getFavoriteMap(userId, performances);
        List<ArtistPerformancesDetailDTO> performanceList = performances.stream()
                .map(performance -> {
                    String key = performance.typeId() + "_" + performance.type();
                    boolean isFavorite = favoriteMap.getOrDefault(key, false);
                    return ArtistPerformancesDetailDTO.from(performance, isFavorite);
                })
                .toList();

        return ArtistPerformancesDTO.from(performanceList);
    }

    @Transactional(readOnly = true)
    public Map<String, Boolean> getFavoriteMap(Long userId, List<PerformanceDTO> performances) {
        if (userId == null || performances.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<PerformanceType_DEPRECATED, Set<Long>> typeToIdsMap = groupPerformancesByType(performances);
        return fetchFavoriteMap(userId, typeToIdsMap);
    }

    private Map<PerformanceType_DEPRECATED, Set<Long>> groupPerformancesByType(List<PerformanceDTO> performances) {
        Map<PerformanceType_DEPRECATED, Set<Long>> typeToIdsMap = new HashMap<>();

        for (PerformanceDTO performance : performances) {
            if (!typeToIdsMap.containsKey(performance.type())) {
                typeToIdsMap.put(performance.type(), new HashSet<>());
            }
            typeToIdsMap.get(performance.type()).add(performance.typeId());
        }

        return typeToIdsMap;
    }

    private Map<String, Boolean> fetchFavoriteMap(Long userId, Map<PerformanceType_DEPRECATED, Set<Long>> typeToIdsMap) {
        Map<String, Boolean> favoriteMap = new HashMap<>();

        typeToIdsMap.forEach((type, ids) -> {
            if (!ids.isEmpty()) {
                List<Long> favoriteIds = findFavoritesByType(userId, type, ids);
                favoriteIds.forEach(id -> favoriteMap.put(id + "_" + type, true));
            }
        });

        return favoriteMap;
    }

    private List<Long> findFavoritesByType(Long userId, PerformanceType_DEPRECATED type, Set<Long> ids) {
        return switch (type) {
            case CONCERT -> concertFavoriteService.findFavorites(userId, ids);
            case FESTIVAL -> festivalFavoriteService.findFavorites(userId, ids);
            default -> List.of();
        };
    }

    @Transactional(readOnly = true)
    public RecommendPerformancesDTO getRecommendPerformances() {
        return RecommendPerformancesDTO.of(
                performanceService.getRecommendExpectedPerformances(),
                s3FileHandler
        );
    }

    @Transactional(readOnly = true)
    public SearchACPerformancesDTO searchACPerformances(String term, int limit, PerformanceStatus status) {
        return SearchACPerformancesDTO.from(
                performanceSearchService.getPerformancesByTitle(term, limit, status)
        );
    }

    @Transactional(readOnly = true)
    public Optional<RecommendMusicsPerformanceDTO> getRecommendExpectedPerformanceId(Long userId) {

        Optional<Performance> recommendPerformance = getRecommendExpectedFavoritePerformance(userId);

        if (recommendPerformance.isEmpty()) {
            recommendPerformance = getRecommentExpectedPerformance();
        }

        return recommendPerformance.map(RecommendMusicsPerformanceDTO::from);
    }

    private Optional<Performance> getRecommendExpectedFavoritePerformance(Long userId) {
        if (Objects.nonNull(userId)) {
            return performanceService.getRecommendExpectedFavoritePerformance(userId);
        }

        return Optional.empty();
    }

    private Optional<Performance> getRecommentExpectedPerformance() {
        return performanceService.getRecommentExpectedPerformance();
    }

    protected Set<String> setArtistsByRandom(Performance_DPRECATED performanceDPRECATED) {
        List<PerformanceArtist_DPRECATED> performanceArtistDPRECATEDS = performanceDPRECATED.getArtists();

        if (performanceArtistDPRECATEDS.isEmpty()) {
            return Collections.emptySet();
        }

        List<String> artistList = performanceArtistDPRECATEDS.stream()
                .map(PerformanceArtist_DPRECATED::getArtistId).distinct().collect(Collectors.toList());
        Collections.shuffle(artistList);

        int artistCount = Math.min(artistList.size(), 3);
        return new HashSet<>(artistList.subList(0, artistCount));
    }

    @Transactional(readOnly = true)
    public RecommendMusicsDTO getNewRecommendMusics(long performanceId, List<String> musicIds) {
        Performance_DPRECATED performanceDPRECATED = performanceServiceDPRECATED.getPerformanceById(performanceId);
        Set<String> selectedArtistIds = setArtistsByRandom(performanceDPRECATED);
        Set<String> existingMusicIds = (musicIds == null || musicIds.isEmpty())
                ? Collections.emptySet()
                : musicIds.stream().flatMap(ids -> Arrays.stream(ids.split(",")))
                        .map(String::trim).collect(Collectors.toSet());

        List<String> artistIdList = new ArrayList<>(selectedArtistIds);
        List<ConfetiMusic> recommendMusics = recommendMusicsByArtistCount(artistIdList, existingMusicIds);

        return RecommendMusicsDTO.from(recommendMusics);
    }

    private List<ConfetiMusic> recommendMusicsByArtistCount(List<String> artistIdList, Set<String> existingMusicIds) {
        int artistCount = artistIdList.size();
        if (artistCount == 1) {
            return recommendForSingleArtist(artistIdList, existingMusicIds);
        }
        if (artistCount == 2) {
            return recommendForTwoArtists(artistIdList, existingMusicIds);
        }
        return recommendForMultipleArtists(artistIdList, existingMusicIds);
    }

    private List<ConfetiMusic> recommendForSingleArtist(List<String> artistIdList, Set<String> existingMusicIds) {
        return musicAPIHandler.getFilteredTopSongsByArtist(
                artistIdList.getFirst(), RECOMMEND_MUSIC_SIZE, existingMusicIds
        );
    }

    private List<ConfetiMusic> recommendForTwoArtists(List<String> artistIdList, Set<String> existingMusicIds) {
        List<ConfetiMusic> musics = new ArrayList<>();
        musics.addAll(musicAPIHandler.getFilteredTopSongsByArtist(artistIdList.getFirst(), 2, existingMusicIds));
        musics.addAll(musicAPIHandler.getFilteredTopSongsByArtist(artistIdList.getLast(), 1, existingMusicIds));
        return musics;
    }

    private List<ConfetiMusic> recommendForMultipleArtists(List<String> artistIdList, Set<String> existingMusicIds) {
        List<ConfetiMusic> musics = new ArrayList<>();
        for (String artistId : artistIdList) {
            if (musics.size() >= RECOMMEND_MUSIC_SIZE) {
                break;
            }
            musics.addAll(musicAPIHandler.getFilteredTopSongsByArtist(artistId, 1, existingMusicIds));
        }
        return musics;
    }

    @Transactional(readOnly = true)
    public ConfetiRecordDTO getConfetiRecord(long userId) {
        validateExistUser(userId);

        List<Long> timetableFestivalIds = timetableFestivalService.findFestivalIdsByUserId(userId);
        List<Long> setListFestivalIds = setlistService.findFestivalIdsByUserId(userId);
        List<Long> setListConcertIds = setlistService.findConcertIdsByUserId(userId);

        Set<Long> uniqueFestivalIds = new HashSet<>();
        uniqueFestivalIds.addAll(timetableFestivalIds);
        uniqueFestivalIds.addAll(setListFestivalIds);

        int totalCount = uniqueFestivalIds.size() + setListConcertIds.size();

        return ConfetiRecordDTO.of(totalCount, timetableFestivalIds.size(),
                setListFestivalIds.size() + setListConcertIds.size());
    }

    protected void validateExistUser(long userId) {
        if (!userService.existsById(userId)) {
            throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED);
        }
    }

    @Transactional(readOnly = true)
    public ExpectedPerformancesDTO getExpectedPerformances(GetExpectedPerformancesDTO expectedPerformancesDTO) {
        List<Long> performanceIds = expectedPerformancesDTO.expectedPerformanceDTOs().stream()
                .map(GetExpectedPerformanceDTO::performanceId)
                .toList();

        List<Performance> performances = performanceService.getExpectedPerformancesIn(performanceIds);

        return ExpectedPerformancesDTO.of(performances, s3FileHandler);
    }

    public PerformanceIdsDTO getPerformances() {
        return PerformanceIdsDTO.from(
                performanceServiceDPRECATED.getPerformances()
        );
    }
}

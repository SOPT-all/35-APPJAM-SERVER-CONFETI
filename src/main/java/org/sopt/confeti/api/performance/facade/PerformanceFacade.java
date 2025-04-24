package org.sopt.confeti.api.performance.facade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.performance.facade.dto.response.AnalyzePerformanceTypeDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformancesDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConfetiRecordDTO;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.IntendedPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendMusicsPerformanceDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendMusicsDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.SearchACPerformancesDTO;
import org.sopt.confeti.domain.artist_favorite.ArtistFavorite;
import org.sopt.confeti.domain.artist_favorite.application.ArtistFavoriteService;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.application.ConcertService;
import org.sopt.confeti.domain.concert_favorite.application.ConcertFavoriteService;
import org.sopt.confeti.domain.elastic_search.application.PerformanceSearchService;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.festival_favorite.application.FestivalFavoriteService;
import org.sopt.confeti.domain.setlist.application.SetlistService;
import org.sopt.confeti.domain.timetable_festival.application.TimetableFestivalService;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.domain.view.performance.Performance;
import org.sopt.confeti.domain.view.performance.PerformanceArtist;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;
import org.sopt.confeti.domain.view.performance.application.PerformanceService;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.common.constant.PerformanceStatus;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.exception.ConfetiException;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import org.sopt.confeti.global.util.MorphemeAnalyzer;
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
    private static final String TYPE_ALL = "ALL";

    private final ConcertService concertService;
    private final FestivalService festivalService;
    private final UserService userService;
    private final FestivalFavoriteService festivalFavoriteService;
    private final PerformanceService performanceService;
    private final ConcertFavoriteService concertFavoriteService;
    private final ArtistFavoriteService artistFavoriteService;
    private final PerformanceSearchService performanceSearchService;
    private final MusicAPIHandler musicAPIHandler;
    private final TimetableFestivalService timetableFestivalService;
    private final SetlistService setlistService;

    @Transactional(readOnly = true)
    public ConcertDetailDTO getConcertDetailInfo(final Long userId, final long concertId) {
        Concert concert = concertService.getConcertDetailByConcertId(concertId);
        validateConcertNotPassed(concert);

        return ConcertDetailDTO.of(concert, getConcertFavorite(userId, concertId));
    }

    @Transactional(readOnly = true)
    public boolean getConcertFavorite(final Long userId, final long concertId) {
        if (userId == null) {
            return false;
        }

        return concertFavoriteService.isFavorite(userId, concertId);
    }

    @Transactional(readOnly = true)
    protected void validateConcertNotPassed(final Concert concert) {
        if (LocalDate.now().isAfter(concert.getEndAt())) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    public FestivalDetailDTO getFestivalDetailInfo(final Long userId, final long festivalId) {
        boolean isFavorite = getIsFavorite(userId, festivalId);

        Festival festival = festivalService.getFestivalDetailByFestivalId(festivalId);
        validateFestivalNotPassed(festival);

        return FestivalDetailDTO.of(festival, isFavorite);
    }

    @Transactional(readOnly = true)
    protected boolean getIsFavorite(final Long userid, final long festivalId) {
        if (userid != null) {
            return festivalFavoriteService.isFavorite(userid, festivalId);
        }

        return false;
    }

    @Transactional(readOnly = true)
    protected void validateFestivalNotPassed(final Festival festival) {
        if (LocalDate.now().isAfter(festival.getEndAt())) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    public PerformanceReservationDTO getPerformReservationInfo(final Long userId) {
        boolean isUserExist = userId != null && userService.existsById(userId);
        boolean isCFExist = isUserExist && concertFavoriteService.existsByUserId(userId);
        boolean isFFExist = isUserExist && festivalFavoriteService.existsByUserId(userId);

        if (isCFExist || isFFExist) {
            List<PerformanceTicketDTO> performanceReserve = performanceService.getFavoritePerformancesReservation(
                    userId);
            return PerformanceReservationDTO.from(performanceReserve);
        }

        List<PerformanceTicketDTO> performanceReserve = performanceService.getPerformancesReservation();
        return PerformanceReservationDTO.from(performanceReserve);
    }

    @Transactional(readOnly = true)
    public RecentPerformancesDTO getRecentPerformances(final Long userId) {
        if (userId == null || !hasFavoriteArtists(userId)) {
            return getRecentPerformancesWithoutFavorites();
        }

        RecentPerformancesDTO recentPerformances = getRecentPerformancesWithFavorites(userId);

        if (recentPerformances.performances().isEmpty()) {
            return getRecentPerformancesWithoutFavorites();
        }

        return recentPerformances;
    }

    @Transactional(readOnly = true)
    public RecentPerformancesDTO getRecentPerformancesWithFavorites(final long userId) {
        List<ArtistFavorite> artistFavorites = artistFavoriteService.getArtistIdsByUserId(userId);

        return RecentPerformancesDTO.of(
                PERSONALIZED,
                performanceService.getPerformancesByArtistIds(
                        artistFavorites.stream()
                                .map(artistFavorite -> artistFavorite.getArtist().getId())
                                .toList(),
                        RECENT_PERFORMANCES_SIZE
                )
        );
    }

    @Transactional(readOnly = true)
    public RecentPerformancesDTO getRecentPerformancesWithoutFavorites() {
        List<Performance> performances = performanceService.getRecentPerformances(RECENT_PERFORMANCES_SIZE);
        return RecentPerformancesDTO.of(
                UNPERSONALIZED,
                performances
        );
    }

    @Transactional(readOnly = true)
    public boolean hasFavoriteArtists(final long userId) {
        return artistFavoriteService.existsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public ArtistPerformancesDTO getPerformancesByArtistId(final Long userId, final String artistId) {
        List<PerformanceDTO> performances = performanceService.findPerformancesByArtistId(artistId);

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
    public Map<String, Boolean> getFavoriteMap(final Long userId, List<PerformanceDTO> performances) {
        if (userId == null || performances.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<PerformanceType, Set<Long>> typeToIdsMap = groupPerformancesByType(performances);
        return fetchFavoriteMap(userId, typeToIdsMap);
    }

    private Map<PerformanceType, Set<Long>> groupPerformancesByType(List<PerformanceDTO> performances) {
        Map<PerformanceType, Set<Long>> typeToIdsMap = new HashMap<>();

        for (PerformanceDTO performance : performances) {
            if (!typeToIdsMap.containsKey(performance.type())) {
                typeToIdsMap.put(performance.type(), new HashSet<>());
            }
            typeToIdsMap.get(performance.type()).add(performance.typeId());
        }

        return typeToIdsMap;
    }

    private Map<String, Boolean> fetchFavoriteMap(Long userId, Map<PerformanceType, Set<Long>> typeToIdsMap) {
        Map<String, Boolean> favoriteMap = new HashMap<>();

        typeToIdsMap.forEach((type, ids) -> {
            if (!ids.isEmpty()) {
                List<Long> favoriteIds = findFavoritesByType(userId, type, ids);
                favoriteIds.forEach(id -> favoriteMap.put(id + "_" + type, true));
            }
        });

        return favoriteMap;
    }

    private List<Long> findFavoritesByType(Long userId, PerformanceType type, Set<Long> ids) {
        return switch (type) {
            case CONCERT -> concertFavoriteService.findFavorites(userId, ids);
            case FESTIVAL -> festivalFavoriteService.findFavorites(userId, ids);
            default -> List.of();
        };
    }

    @Transactional(readOnly = true)
    public RecommendPerformancesDTO getRecommendPerformances() {
        return RecommendPerformancesDTO.from(
                performanceService.getRecommendPerformances()
        );
    }

    @Transactional(readOnly = true)
    public SearchACPerformancesDTO searchACPerformances(String term, int limit, PerformanceStatus status) {
        return SearchACPerformancesDTO.from(
                performanceSearchService.getPerformancesByTitle(term, limit, status)
        );
    }

    @Transactional(readOnly = true)
    public IntendedPerformancesDTO getPerformances(Long userId, Long pid, String aid, String ptitle,
                                                   PerformanceType ptype) {
        if (!isPresent(pid) && !isPresent(aid) && !isPresent(ptitle)) {
            throw new ConfetiException(ErrorMessage.BAD_REQUEST);
        }

        List<PerformanceDTO> performances = new ArrayList<>();

        if (isPresent(pid)) {
            performances.add(performanceService.getPerformance(pid));
        }

        if (isPresent(aid)) {
            performances.addAll(performanceService.getPerformancesByArtistIdAndType(aid, ptype));
        }

        if (isPresent(ptitle)) {
            List<PerformanceDTO> searchedPerformances = performanceSearchService.getPerformancesByTitleAndTypePartialMatched(
                            ptitle, ptype).stream()
                    .map(PerformanceDTO::from)
                    .toList();

            performances.addAll(searchedPerformances);
        }

        Map<Long, Boolean> performanceFavorites = getPerformanceFavorites(userId, performances);
        return IntendedPerformancesDTO.of(
                new HashSet<>(performances),
                performanceFavorites
        );
    }

    private boolean isPresent(Object target) {
        return Objects.nonNull(target);
    }

    private Map<Long, Boolean> getPerformanceFavorites(Long userId, List<PerformanceDTO> performances) {
        Map<Long, Boolean> performanceFavorites = new HashMap<>();
        performances.forEach(performance -> performanceFavorites.put(performance.id(), false));

        if (isPresent(userId)) {
            List<PerformanceDTO> favoritePerformances = performanceService.getFavoritePerformancesAll(userId, TYPE_ALL);

            favoritePerformances.forEach(favoritePerformance -> {
                if (performanceFavorites.containsKey(favoritePerformance.id())) {
                    performanceFavorites.put(favoritePerformance.id(), true);
                }
            });
        }

        return performanceFavorites;
    }

    public AnalyzePerformanceTypeDTO analyzePerformanceType(String term) {
        KomoranResult analyzeResult = MorphemeAnalyzer.getAnalyzeResult(term);
        PerformanceType performanceType = MorphemeAnalyzer.getFirstMatchingPerformanceType(analyzeResult);
        String processedTerm = MorphemeAnalyzer.getRemovedPerformanceTypesTerm(term, analyzeResult);

        return AnalyzePerformanceTypeDTO.of(processedTerm, performanceType);
    }

    @Transactional(readOnly = true)
    public RecommendMusicsPerformanceDTO getRecommendPerformanceId(final Long userId) {

        Performance performance = performanceService.getPerformanceByUserFavorites(userId);
        if (userId == null || performance == null) {
            performance = performanceService.getPerformanceByRand();
        }

        return RecommendMusicsPerformanceDTO.from(performance);
    }

    protected Set<String> setArtistsByRandom(Performance performance) {
        List<PerformanceArtist> performanceArtists = performance.getArtists();

        if (performanceArtists.isEmpty()) {
            return Collections.emptySet();
        }

        List<String> artistList = performanceArtists.stream()
                .map(PerformanceArtist::getArtistId).distinct().collect(Collectors.toList());
        Collections.shuffle(artistList);

        int artistCount = Math.min(artistList.size(), 3);
        return new HashSet<>(artistList.subList(0, artistCount));
    }

    @Transactional(readOnly = true)
    public RecommendMusicsDTO getNewRecommendMusics(long performanceId, List<String> musicIds) {
        Performance performance = performanceService.getPerformanceById(performanceId);
        Set<String> selectedArtistIds = setArtistsByRandom(performance);
        Set<String> existingMusicIds = (musicIds == null || musicIds.isEmpty())
                ? Collections.emptySet()
                : new HashSet<>(musicIds);

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
    public ConfetiRecordDTO getConfetiRecord(final long userId) {
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

    protected void validateExistUser(final long userId) {
        if (!userService.existsById(userId)) {
            throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED);
        }
    }
}

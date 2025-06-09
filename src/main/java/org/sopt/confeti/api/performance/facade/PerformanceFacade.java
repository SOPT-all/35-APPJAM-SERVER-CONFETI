package org.sopt.confeti.api.performance.facade;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.performance.facade.dto.request.GetExpectedPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformancesDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConfetiRecordDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ExpectedPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendMusicsDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendMusicsPerformanceDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.SearchACPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceIdsDTO;
import org.sopt.confeti.api.performance.vo.UserPerformanceRecordVO;
import org.sopt.confeti.domain.artist_favorite.ArtistFavorite;
import org.sopt.confeti.domain.artist_favorite.application.ArtistFavoriteService;
import org.sopt.confeti.domain.concert.Concert;
import org.sopt.confeti.domain.concert.application.ConcertService;
import org.sopt.confeti.domain.concert_favorite.application.ConcertFavoriteService;
import org.sopt.confeti.domain.elastic_search.application.PerformanceSearchService;
import org.sopt.confeti.domain.elastic_search.application.SearchTermService;
import org.sopt.confeti.domain.festival.Festival;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.festival_favorite.application.FestivalFavoriteService;
import org.sopt.confeti.domain.setlist.SetlistType;
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
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.music.vo.ConfetiMusic;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Facade
@RequiredArgsConstructor
public class PerformanceFacade {

    private static final int RECENT_PERFORMANCES_SIZE = 7;
    private static final int RECOMMEND_MUSIC_SIZE = 3;
    private static final int MUSIC_FETCH_SIZE = 5;
    private static final int SELECTED_ARTISTS_SIZE = 3;
    private static final boolean PERSONALIZED = true;
    private static final boolean UNPERSONALIZED = false;

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
    private final SearchTermService searchTermService;

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
        boolean isCFExist = isUserExist && concertFavoriteService.existsUpcomingReservationByUserId(userId);
        boolean isFFExist = isUserExist && festivalFavoriteService.existsUpcomingReservationByUserId(userId);

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
        List<PerformanceDTO> performances = performanceService.getPerformancesByArtistId(artistId);

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

    public Optional<RecommendMusicsPerformanceDTO> getRecommendPerformanceIdByRand() {
        return performanceService.getPerformanceByRand()
                .map(RecommendMusicsPerformanceDTO::from);
    }

    public Optional<RecommendMusicsPerformanceDTO> getRecommendPerformanceIdByUserId(final Long userId) {
        return performanceService.getPerformanceByUserFavorites(userId)
                .map(RecommendMusicsPerformanceDTO::from);
    }

    @Transactional(readOnly = true)
    public RecommendMusicsDTO getNewRecommendMusics(long performanceId, List<String> musicIds) {
        Performance performance = performanceService.getPerformanceById(performanceId);
        Set<String> selectedArtistIds = selectRandomArtistIds(performance);
        Set<String> existingMusicIds = (musicIds == null || musicIds.isEmpty())
                ? Collections.emptySet()
                : musicIds.stream().flatMap(ids -> Arrays.stream(ids.split(",")))
                        .map(String::trim).collect(Collectors.toSet());

        List<String> artistIdList = new ArrayList<>(selectedArtistIds);
        List<ConfetiMusic> recommendMusics = recommendMusicsByArtistCount(artistIdList, existingMusicIds);

        return RecommendMusicsDTO.from(recommendMusics);
    }

    protected Set<String> selectRandomArtistIds(Performance performance) {
        List<PerformanceArtist> performanceArtists = performance.getArtists();

        if (performanceArtists.isEmpty()) {
            return Collections.emptySet();
        }

        List<String> artistList = performanceArtists.stream()
                .map(PerformanceArtist::getArtistId).distinct().collect(Collectors.toList());
        Collections.shuffle(artistList);

        int artistCount = Math.min(artistList.size(), SELECTED_ARTISTS_SIZE);
        return new HashSet<>(artistList.subList(0, artistCount));
    }

    private List<ConfetiMusic> recommendMusicsByArtistCount(List<String> artistIdList, Set<String> existingMusicIds) {
        List<ConfetiMusic> musics = new ArrayList<>();
        Set<String> seenMusicIds = new HashSet<>(existingMusicIds);
        int round = 0;

        while (musics.size() < RECOMMEND_MUSIC_SIZE && round < MUSIC_FETCH_SIZE) {
            for (String artistId : artistIdList) {
                if (musics.size() >= RECOMMEND_MUSIC_SIZE) break;

                List<ConfetiMusic> topSongs = musicAPIHandler.getFilteredTopSongsByArtist(
                        artistId, MUSIC_FETCH_SIZE, seenMusicIds
                );

                if (round < topSongs.size()) {
                    ConfetiMusic song = topSongs.get(round);
                    if (!seenMusicIds.contains(song.getId())) {
                        musics.add(song);
                        seenMusicIds.add(song.getId());
                    }
                }
            }
            round++;
        }

        return musics;
    }

    public ConfetiRecordDTO getConfetiRecord(final long userId) {
        validateExistUser(userId);

        List<Long> timetableFestivalIds = timetableFestivalService.findFestivalIdsByUserId(userId);
        List<Long> setListFestivalIds = setlistService.findMusicIdsByUserId(userId, SetlistType.FESTIVAL);
        List<Long> setListConcertIds = setlistService.findMusicIdsByUserId(userId, SetlistType.CONCERT);

        UserPerformanceRecordVO record = new UserPerformanceRecordVO(
                timetableFestivalIds,
                setListFestivalIds,
                setListConcertIds
        );

        return ConfetiRecordDTO.from(record);
    }

    protected void validateExistUser(final long userId) {
        if (!userService.existsById(userId)) {
            throw new UnauthorizedException(ErrorMessage.UNAUTHORIZED);
        }
    }

    @Transactional(readOnly = true)
    public ExpectedPerformancesDTO getExpectedPerformances(GetExpectedPerformancesDTO expectedPerformancesDTO) {
        return ExpectedPerformancesDTO.from(performanceService.getExpectedPerformances(expectedPerformancesDTO));
    }

    public PerformanceIdsDTO getPerformances() {
        return PerformanceIdsDTO.from(
                performanceService.getPerformances()
        );
    }
}

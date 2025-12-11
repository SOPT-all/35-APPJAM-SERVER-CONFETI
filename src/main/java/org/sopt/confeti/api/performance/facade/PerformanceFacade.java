package org.sopt.confeti.api.performance.facade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.performance.facade.dto.request.GetExpectedPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformancesDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConfetiRecordDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ExpectedPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceIdsDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceRecommendDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformancesRecommendDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendSongsDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendSongsPerformanceDTO;
import org.sopt.confeti.api.performance.facade.dto.response.SearchACPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.SongRecommendDTO;
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
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceArtistDTO;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.common.ExecutorName;
import org.sopt.confeti.global.common.constant.PerformanceStatus;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Facade
public class PerformanceFacade {

    private static final int RECENT_PERFORMANCES_SIZE = 7;
    private static final int RECOMMEND_SONG_SIZE = 3;
    private static final int RECOMMEND_SONG_FETCH_SIZE = 20;
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
    private final Executor performanceExecutor;

    public PerformanceFacade(ConcertService concertService,
        FestivalService festivalService,
        UserService userService,
        FestivalFavoriteService festivalFavoriteService,
        PerformanceService performanceService,
        ConcertFavoriteService concertFavoriteService,
        ArtistFavoriteService artistFavoriteService,
        PerformanceSearchService performanceSearchService,
        MusicAPIHandler musicAPIHandler,
        TimetableFestivalService timetableFestivalService,
        SetlistService setlistService,
        @Qualifier(ExecutorName.PERFORMANCE_EXECUTOR) Executor performanceExecutor) {
        this.concertService = concertService;
        this.festivalService = festivalService;
        this.userService = userService;
        this.festivalFavoriteService = festivalFavoriteService;
        this.performanceService = performanceService;
        this.concertFavoriteService = concertFavoriteService;
        this.artistFavoriteService = artistFavoriteService;
        this.performanceSearchService = performanceSearchService;
        this.musicAPIHandler = musicAPIHandler;
        this.timetableFestivalService = timetableFestivalService;
        this.setlistService = setlistService;
        this.performanceExecutor = performanceExecutor;
    }

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
        boolean isCFExist =
            isUserExist && concertFavoriteService.existsUpcomingReservationByUserId(userId);
        boolean isFFExist =
            isUserExist && festivalFavoriteService.existsUpcomingReservationByUserId(userId);

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
        List<Performance> performances = performanceService.getRecentPerformances(
            RECENT_PERFORMANCES_SIZE);
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
    public ArtistPerformancesDTO getPerformancesByArtistId(final Long userId,
        final String artistId) {
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
    public Map<String, Boolean> getFavoriteMap(final Long userId,
        List<PerformanceDTO> performances) {
        if (userId == null || performances.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<PerformanceType, Set<Long>> typeToIdsMap = groupPerformancesByType(performances);
        return fetchFavoriteMap(userId, typeToIdsMap);
    }

    private Map<PerformanceType, Set<Long>> groupPerformancesByType(
        List<PerformanceDTO> performances) {
        Map<PerformanceType, Set<Long>> typeToIdsMap = new HashMap<>();

        for (PerformanceDTO performance : performances) {
            if (!typeToIdsMap.containsKey(performance.type())) {
                typeToIdsMap.put(performance.type(), new HashSet<>());
            }
            typeToIdsMap.get(performance.type()).add(performance.typeId());
        }

        return typeToIdsMap;
    }

    private Map<String, Boolean> fetchFavoriteMap(Long userId,
        Map<PerformanceType, Set<Long>> typeToIdsMap) {
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

    @Deprecated
    @Transactional(readOnly = true)
    public RecommendPerformancesDTO getRecommendPerformances() {
        return RecommendPerformancesDTO.from(
            performanceService.getRecommendPerformances()
        );
    }

    @Transactional(readOnly = true)
    public RecommendPerformancesDTO getRecommendPerformances(int limit) {
        return RecommendPerformancesDTO.from(
            performanceService.getRecommendPerformances(limit)
        );
    }

    @Transactional(readOnly = true)
    public SearchACPerformancesDTO searchACPerformances(String term, int limit,
        PerformanceStatus status) {
        return SearchACPerformancesDTO.from(
            performanceSearchService.getPerformancesByTitle(term, limit, status)
        );
    }

    @Transactional(readOnly = true)
    public Optional<RecommendSongsPerformanceDTO> getRecommendPerformanceId(final Long userId) {

        Optional<Performance> performance = performanceService.getPerformanceByUserFavorites(
            userId);
        if (Objects.isNull(userId) || performance.isEmpty()) {
            performance = performanceService.getPerformanceByRand();
        }

        return performance.map(RecommendSongsPerformanceDTO::from);
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

    @Deprecated
    @Transactional(readOnly = true)
    public RecommendSongsDTO getNewRecommendSongs(long performanceId, List<String> songIds) {
        Performance performance = performanceService.getPerformanceById(performanceId);
        Set<String> selectedArtistIds = setArtistsByRandom(performance);
        Set<String> existingSongIds = (songIds == null || songIds.isEmpty())
            ? Collections.emptySet()
            : songIds.stream().flatMap(ids -> Arrays.stream(ids.split(",")))
                .map(String::trim).collect(Collectors.toSet());

        List<String> artistIdList = new ArrayList<>(selectedArtistIds);
        List<ConfetiSong> recommendSongs = recommendSongsByArtistCount(artistIdList,
            existingSongIds);

        return RecommendSongsDTO.from(recommendSongs);
    }

    @Deprecated
    private List<ConfetiSong> recommendSongsByArtistCount(List<String> artistIdList,
        Set<String> existingSongIds) {
        int artistCount = artistIdList.size();
        if (artistCount == 1) {
            return recommendForSingleArtist(artistIdList, existingSongIds);
        }
        if (artistCount == 2) {
            return recommendForTwoArtists(artistIdList, existingSongIds);
        }
        return recommendForMultipleArtists(artistIdList, existingSongIds);
    }

    @Deprecated
    private List<ConfetiSong> recommendForSingleArtist(List<String> artistIdList,
        Set<String> existingSongIds) {
        return musicAPIHandler.getFilteredTopSongsByArtist(
            artistIdList.getFirst(), RECOMMEND_SONG_SIZE, existingSongIds
        );
    }

    @Deprecated
    private List<ConfetiSong> recommendForTwoArtists(List<String> artistIdList,
        Set<String> existingSongIds) {
        List<ConfetiSong> songs = new ArrayList<>();
        songs.addAll(musicAPIHandler.getFilteredTopSongsByArtist(artistIdList.getFirst(), 2,
            existingSongIds));
        songs.addAll(musicAPIHandler.getFilteredTopSongsByArtist(artistIdList.getLast(), 1,
            existingSongIds));
        return songs;
    }

    @Deprecated
    private List<ConfetiSong> recommendForMultipleArtists(List<String> artistIdList,
        Set<String> existingSongIds) {
        List<ConfetiSong> songs = new ArrayList<>();
        for (String artistId : artistIdList) {
            if (songs.size() >= RECOMMEND_SONG_SIZE) {
                break;
            }
            songs.addAll(
                musicAPIHandler.getFilteredTopSongsByArtist(artistId, 1, existingSongIds));
        }
        return songs;
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

    @Transactional(readOnly = true)
    public ExpectedPerformancesDTO getExpectedPerformances(
        GetExpectedPerformancesDTO expectedPerformancesDTO) {
        return ExpectedPerformancesDTO.from(
            performanceService.getExpectedPerformances(expectedPerformancesDTO));
    }

    public PerformanceIdsDTO getPerformances() {
        return PerformanceIdsDTO.from(
            performanceService.getPerformances()
        );
    }

    public PerformancesRecommendDTO getPerformancesRecommend(Long userId, int performanceLimit,
        int songLimit) {
        List<PerformanceDTO> performances = getFavoritePerformanceRecommend(userId,
            performanceLimit);

        if (performances.isEmpty()) {
            performances = performanceService.getRandomUpcomingPerformances(performanceLimit);
        }

        List<CompletableFuture<PerformanceRecommendDTO>> futures = performances.stream()
            .map(performance -> CompletableFuture.supplyAsync(
                    () -> getPerformanceRecommend(performance, songLimit), performanceExecutor)
                .exceptionally(ex -> {
                    log.warn(
                        "PerformanceFacade.getPerformancesRecommend : Performance id : {}, Message : {}",
                        performance.id(), ex.getMessage());
                    return PerformanceRecommendDTO.of(performance, Collections.emptyList());
                })
            )
            .toList();

        List<PerformanceRecommendDTO> performancesRecommend = futures.stream()
            .map(CompletableFuture::join)
            .toList();

        return PerformancesRecommendDTO.from(performancesRecommend);
    }

    private List<PerformanceDTO> getFavoritePerformanceRecommend(Long userId,
        int performanceLimit) {
        if (userId == null) {
            return Collections.emptyList();
        }

        List<Long> festivalFavoriteIds = festivalFavoriteService.getRandomFavoriteUpcomingFestivalIds(
            userId, performanceLimit);
        List<Long> concertFavoriteIds = concertFavoriteService.getRandomFavoriteUpcomingConcertIds(
            userId, performanceLimit);

        List<PerformanceDTO> festivalPerformances = performanceService.getPerformancesByTypeAndTypeIds(
            PerformanceType.FESTIVAL, festivalFavoriteIds);
        List<PerformanceDTO> concertPerformances = performanceService.getPerformancesByTypeAndTypeIds(
            PerformanceType.CONCERT, concertFavoriteIds);
        List<PerformanceDTO> recommendPerformances = new ArrayList<>(
            Stream.concat(festivalPerformances.stream(), concertPerformances.stream()).toList());
        Collections.shuffle(recommendPerformances);

        return recommendPerformances.stream()
            .limit(performanceLimit)
            .toList();
    }

    private PerformanceRecommendDTO getPerformanceRecommend(PerformanceDTO performance,
        int songLimit) {
        List<SongRecommendDTO> songsRecommend = getSongsRecommend(
            performanceService.getRandomPerformanceArtists(performance.id(), songLimit), songLimit
        );

        return PerformanceRecommendDTO.of(performance, songsRecommend);
    }

    private List<SongRecommendDTO> getSongsRecommend(List<PerformanceArtistDTO> artists,
        int songLimit) {
        List<CompletableFuture<List<ConfetiSong>>> futures = artists.stream()
            .map(artist -> CompletableFuture.supplyAsync(
                () -> getArtistTopSongs(artist),
                performanceExecutor))
            .toList();

        List<ConfetiSong> topSongs = futures.stream()
            .map(CompletableFuture::join)
            .flatMap(Collection::stream)
            .toList();

        return pickRandomSongs(topSongs, songLimit).stream()
            .map(SongRecommendDTO::from)
            .toList();
    }

    private List<ConfetiSong> getArtistTopSongs(PerformanceArtistDTO artist) {
        return musicAPIHandler.getArtistTopSongs(artist.artistId(), RECOMMEND_SONG_FETCH_SIZE);
    }

    private List<ConfetiSong> pickRandomSongs(List<ConfetiSong> songs, int size) {
        List<ConfetiSong> copiedSongs = new ArrayList<>(songs);
        Collections.shuffle(copiedSongs);
        return copiedSongs.subList(0, Math.min(size, copiedSongs.size()));
    }
}

package org.sopt.confeti.api.performance.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.performance.facade.context.PerformanceReservationContext;
import org.sopt.confeti.api.performance.facade.context.RecentPerformanceContext;
import org.sopt.confeti.api.performance.facade.dto.request.GetUpcomingPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ArtistPerformancesDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConcertDetailWithFavoriteDTO;
import org.sopt.confeti.api.performance.facade.dto.response.ConfetiRecordDTO;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailDTO;
import org.sopt.confeti.api.performance.facade.dto.response.FestivalDetailWithFavoriteDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceIdsDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceRecommendDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformanceReservationDTO;
import org.sopt.confeti.api.performance.facade.dto.response.PerformancesRecommendDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecentPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.RecommendPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.SearchACPerformanceDTO;
import org.sopt.confeti.api.performance.facade.dto.response.SearchACPerformancesDTO;
import org.sopt.confeti.api.performance.facade.dto.response.SongRecommendDTO;
import org.sopt.confeti.api.performance.facade.dto.response.UpcomingPerformancesDTO;
import org.sopt.confeti.domain.artist_favorite.application.ArtistFavoriteService;
import org.sopt.confeti.domain.concert.application.ConcertService;
import org.sopt.confeti.domain.concert_favorite.application.ConcertFavoriteService;
import org.sopt.confeti.domain.elastic_search.application.PerformanceSearchService;
import org.sopt.confeti.domain.elastic_search.application.dto.response.SearchPerformanceResult;
import org.sopt.confeti.domain.festival.application.FestivalService;
import org.sopt.confeti.domain.festival_favorite.application.FestivalFavoriteService;
import org.sopt.confeti.domain.setlist.application.SetlistService;
import org.sopt.confeti.domain.timetable.application.TimetableService;
import org.sopt.confeti.domain.user.application.UserService;
import org.sopt.confeti.domain.view.performance.PerformanceTicketDTO;
import org.sopt.confeti.domain.view.performance.application.PerformanceFileService;
import org.sopt.confeti.domain.view.performance.application.PerformanceService;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceArtistDTO;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceInfo;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.sopt.confeti.global.common.ExecutorName;
import org.sopt.confeti.global.common.constant.PerformanceStatus;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.exception.UnauthorizedException;
import org.sopt.confeti.global.interceptor.auth.UserContext;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.beans.factory.annotation.Qualifier;

@Slf4j
@Facade
public class PerformanceFacade {

    private static final int RECOMMEND_SONG_FETCH_SIZE = 20;
    private final ConcertService concertService;
    private final FestivalService festivalService;
    private final UserService userService;
    private final FestivalFavoriteService festivalFavoriteService;
    private final PerformanceService performanceService;
    private final ConcertFavoriteService concertFavoriteService;
    private final ArtistFavoriteService artistFavoriteService;
    private final PerformanceSearchService performanceSearchService;
    private final PerformanceFileService performanceFileService;
    private final MusicAPIHandler musicAPIHandler;
    private final TimetableService timetableService;
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
        PerformanceFileService performanceFileService,
        MusicAPIHandler musicAPIHandler,
        TimetableService timetableService,
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
        this.performanceFileService = performanceFileService;
        this.musicAPIHandler = musicAPIHandler;
        this.timetableService = timetableService;
        this.setlistService = setlistService;
        this.performanceExecutor = performanceExecutor;
    }

    public ConcertDetailWithFavoriteDTO getUpcomingConcertDetail(long concertId) {
        ConcertDetailDTO concertDetail = concertService.getUpcomingConcertDetailByConcertId(
            concertId);
        return ConcertDetailWithFavoriteDTO.of(concertDetail, getConcertFavorite(concertId));
    }

    public boolean getConcertFavorite(long concertId) {
        return UserContext.getOptional()
            .map(userInfo -> concertFavoriteService.isFavorite(userInfo.id(), concertId))
            .orElse(false);
    }

    public FestivalDetailWithFavoriteDTO getUpcomingFestivalDetail(long festivalId) {
        FestivalDetailDTO festivalDetail = festivalService.getUpcomingFestivalDetailByFestivalId(
            festivalId);
        return FestivalDetailWithFavoriteDTO.of(festivalDetail, getIsFavorite(festivalId));
    }

    protected boolean getIsFavorite(long festivalId) {
        return UserContext.getOptional()
            .map(userInfo -> festivalFavoriteService.isFavorite(userInfo.id(), festivalId))
            .orElse(false);
    }

    @ReadOnlyTransactional
    public PerformanceReservationDTO getPerformanceReservationInfo() {
        PerformanceReservationContext context = new PerformanceReservationContext();

        UserContext.getOptional().ifPresent(userInfo -> {
            List<PerformanceTicketDTO> favorites =
                performanceService.getFavoritePerformancesReservation(userInfo.id(),
                    PerformanceReservationContext.MAX_SIZE);
            context.addFavoritePerformances(favorites);
        });

        if (!context.isFull()) {
            List<PerformanceTicketDTO> general =
                performanceService.getPerformancesReservationExcluding(
                    context.getExcludedConcertIds(),
                    context.getExcludedFestivalIds(),
                    context.remainingSlots()
                );
            context.addGeneralPerformances(general);
        }

        return context.build();
    }

    @ReadOnlyTransactional
    public RecentPerformancesDTO getRecentPerformances() {
        RecentPerformanceContext context = new RecentPerformanceContext();

        UserContext.getOptional().ifPresent(userInfo -> {
            List<String> favoriteArtistIds =
                artistFavoriteService.getArtistIdsByUserId(userInfo.id());
            if (!favoriteArtistIds.isEmpty()) {
                List<PerformanceInfo> favoritePerformances =
                    performanceService.getPerformancesByArtistIds(
                        favoriteArtistIds,
                        RecentPerformanceContext.MAX_SIZE
                    );
                context.addFavoritePerformances(favoritePerformances);
            }
        });

        if (!context.isFull()) {
            List<PerformanceInfo> general =
                performanceService.getRecentPerformancesExcluding(
                    context.getExcludedConcertIds(),
                    context.getExcludedFestivalIds(),
                    context.remainingSlots()
                );
            context.addGeneralPerformances(general);
        }

        return context.build();
    }

    @ReadOnlyTransactional
    public ArtistPerformancesDTO getPerformancesByArtistId(String artistId) {
        List<PerformanceInfo> performances = performanceService.getPerformancesByArtistId(artistId);

        Map<String, Boolean> favoriteMap = getFavoriteMap(performances);
        List<ArtistPerformancesDetailDTO> performanceList = performances.stream()
            .map(performance -> {
                String key = performance.typeId() + "_" + performance.type();
                boolean isFavorite = favoriteMap.getOrDefault(key, false);
                return ArtistPerformancesDetailDTO.from(performance, isFavorite);
            })
            .toList();

        return ArtistPerformancesDTO.from(performanceList);
    }

    @ReadOnlyTransactional
    public Map<String, Boolean> getFavoriteMap(List<PerformanceInfo> performances) {
        if (performances.isEmpty()) {
            return Collections.emptyMap();
        }

        return UserContext.getOptional()
            .map(userInfo -> fetchFavoriteMap(userInfo.id(), groupPerformancesByType(performances)))
            .orElse(Collections.emptyMap());
    }

    private Map<PerformanceType, Set<Long>> groupPerformancesByType(
        List<PerformanceInfo> performances) {
        Map<PerformanceType, Set<Long>> typeToIdsMap = new HashMap<>();

        for (PerformanceInfo performance : performances) {
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

    @ReadOnlyTransactional
    public RecommendPerformancesDTO getRecommendPerformances(int limit) {
        return RecommendPerformancesDTO.from(
            performanceService.getRecommendPerformances(limit)
        );
    }

    @ReadOnlyTransactional
    public SearchACPerformancesDTO searchACPerformances(String term, int limit,
        PerformanceStatus status) {
        List<SearchPerformanceResult> results = performanceSearchService.getPerformancesByTitle(
            term, limit, status);
        List<SearchACPerformanceDTO> performances = results.stream()
            .map(result -> SearchACPerformanceDTO.of(result,
                performanceFileService.getFileInfo(result.posterPath()).posterUrl()))
            .toList();
        return SearchACPerformancesDTO.from(performances);
    }

    @ReadOnlyTransactional
    public ConfetiRecordDTO getConfetiRecord() {
        long userId = UserContext.get().id();
        List<Long> timetableFestivalIds = timetableService.findFestivalIdsByUserId(userId);
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

    @ReadOnlyTransactional
    public UpcomingPerformancesDTO getUpcomingPerformances(
        GetUpcomingPerformancesDTO upcomingPerformancesDTO) {
        return UpcomingPerformancesDTO.from(
            performanceService.getUpcomingPerformances(upcomingPerformancesDTO));
    }

    public PerformanceIdsDTO getPerformances() {
        return PerformanceIdsDTO.from(
            performanceService.getPerformances()
        );
    }

    public PerformancesRecommendDTO getPerformancesRecommend(int performanceLimit, int songLimit) {
        List<PerformanceInfo> performances = getFavoritePerformanceRecommend(performanceLimit);

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

    private List<PerformanceInfo> getFavoritePerformanceRecommend(int performanceLimit) {
        List<PerformanceInfo> recommendPerformances = UserContext.getOptional()
            .map(userInfo -> getRandomFavoritePerformances(userInfo.id(), performanceLimit))
            .orElse(List.of());

        if (recommendPerformances.isEmpty()) {
            return recommendPerformances;
        }

        Collections.shuffle(recommendPerformances);

        return recommendPerformances.stream()
            .limit(performanceLimit)
            .toList();
    }

    private List<PerformanceInfo> getRandomFavoritePerformances(long userId, int performanceLimit) {
        List<Long> festivalFavoriteIds = festivalFavoriteService.getRandomFavoriteUpcomingFestivalIds(
            userId, performanceLimit);
        List<Long> concertFavoriteIds = concertFavoriteService.getRandomFavoriteUpcomingConcertIds(
            userId, performanceLimit);

        List<PerformanceInfo> festivalPerformances = performanceService.getPerformancesByTypeAndTypeIds(
            PerformanceType.FESTIVAL, festivalFavoriteIds);
        List<PerformanceInfo> concertPerformances = performanceService.getPerformancesByTypeAndTypeIds(
            PerformanceType.CONCERT, concertFavoriteIds);
        return new ArrayList<>(
            Stream.concat(festivalPerformances.stream(), concertPerformances.stream()).toList());
    }

    private PerformanceRecommendDTO getPerformanceRecommend(PerformanceInfo performance,
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

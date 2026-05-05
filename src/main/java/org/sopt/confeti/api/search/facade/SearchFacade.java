package org.sopt.confeti.api.search.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.confeti.api.search.facade.dto.response.PopularTermsDTO;
import org.sopt.confeti.api.search.facade.dto.response.SearchResultDTO;
import org.sopt.confeti.domain.artist_favorite.application.ArtistFavoriteService;
import org.sopt.confeti.domain.concert_favorite.application.ConcertFavoriteService;
import org.sopt.confeti.domain.elastic_search.application.PerformanceSearchService;
import org.sopt.confeti.domain.elastic_search.application.SearchTermService;
import org.sopt.confeti.domain.festival_favorite.application.FestivalFavoriteService;
import org.sopt.confeti.domain.music.application.dto.MusicAPICondition;
import org.sopt.confeti.domain.music.artist.application.ArtistMusicAPIService;
import org.sopt.confeti.domain.music.song.application.SongMusicAPIService;
import org.sopt.confeti.domain.view.performance.application.PerformanceFileService;
import org.sopt.confeti.domain.view.performance.application.PerformanceService;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceArtistDTO;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceInfo;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.interceptor.auth.UserContext;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.resolver.music_api.song.vo.ConfetiSong;
import org.sopt.confeti.global.util.analyzer.SearchTermAnalyzer;
import org.sopt.confeti.global.util.analyzer.dto.PerformanceSearchTermAnalyzeResult;
import org.sopt.confeti.global.util.music.MusicAPIHandler;

@Slf4j
@Facade
@RequiredArgsConstructor
public class SearchFacade {

    private static final String TYPE_ALL = "ALL";
    private static final int RELATED_POPULAR_SONG_LIMIT = 3;
    private static final int RELATED_POPULAR_SONG_ARTIST_LIMIT = 3;

    private final SearchTermService searchTermService;
    private final MusicAPIHandler musicAPIHandler;
    private final ArtistFavoriteService artistFavoriteService;
    private final PerformanceService performanceService;
    private final PerformanceFileService performanceFileService;
    private final FestivalFavoriteService festivalFavoriteService;
    private final ConcertFavoriteService concertFavoriteService;
    private final PerformanceSearchService performanceSearchService;
    private final SongMusicAPIService songMusicAPIService;
    private final ArtistMusicAPIService artistMusicAPIService;

    @ReadOnlyTransactional
    public SearchResultDTO getHomeSearchResultWithAid(String aid) {
        ConfetiArtist artist = getArtistById(aid);
        searchTermService.write(artist.getName());
        boolean artistFavorite = false;

        if (UserContext.exists()) {
            artistFavorite = artistFavoriteService.isFavorite(UserContext.get().id(), aid);
        }

        List<PerformanceInfo> performances = performanceService.getPerformancesByArtistIdAndType(
            aid,
            PerformanceType.PERFORMANCE);
        List<ConfetiSong> songs = songMusicAPIService.getPopularSongsByArtist(artist.getId(),
            RELATED_POPULAR_SONG_LIMIT);

        Map<Long, Boolean> performanceFavorites = performances.stream()
            .collect(Collectors.toMap(
                PerformanceInfo::id,
                p -> false
            ));

        if (UserContext.exists()) {
            List<PerformanceInfo> favoritePerformances = performanceService.getFavoritePerformancesAll(
                UserContext.get().id(), TYPE_ALL);
            getPerformanceFavorites(performanceFavorites, favoritePerformances);
        }

        return SearchResultDTO.of(artist, artistFavorite, performances, performanceFavorites,
            songs);
    }

    @ReadOnlyTransactional
    public SearchResultDTO getHomeSearchResultWithPid(long pid) {
        PerformanceInfo performance = performanceService.getPerformance(pid);
        searchTermService.write(performance.title());
        boolean performanceFavorite = false;

        if (UserContext.exists()) {
            if (performance.type() == PerformanceType.FESTIVAL) {
                performanceFavorite = festivalFavoriteService.isFavorite(UserContext.get().id(),
                    performance.typeId());
            }

            if (performance.type() == PerformanceType.CONCERT) {
                performanceFavorite = concertFavoriteService.isFavorite(UserContext.get().id(),
                    performance.typeId());
            }
        }

        List<ConfetiSong> songs = getPopularSongsByPerformanceArtists(performance);

        return SearchResultDTO.of(performance, performanceFavorite, songs);
    }

    @ReadOnlyTransactional
    public SearchResultDTO getHomeSearchResultWithTerm(String term) {
        PerformanceSearchTermAnalyzeResult analyzeResult = SearchTermAnalyzer.analyzePerformance(
            term);

        Optional<ConfetiArtist> artist = musicAPIHandler.findArtistByKeyword(
            analyzeResult.processedTerm());
        searchTermService.write(artist);
        boolean artistFavorite = false;

        if (UserContext.exists() && artist.isPresent()) {
            artistFavorite = artistFavoriteService.isFavorite(UserContext.get().id(),
                artist.get().getId());
        }

        // 공연은 아티스트 + 검색어 기반
        Set<PerformanceInfo> performances = new HashSet<>();

        // 아티스트 기반
        artist.ifPresent(confetiArtist -> performances.addAll(
                performanceService.getPerformancesByArtistIdAndType(confetiArtist.getId(),
                    analyzeResult.performanceType())
            )
        );

        // 검색어 기반
        List<PerformanceInfo> searchedPerformances = performanceSearchService.getUpcomingPerformancesByTitleAndTypePartialMatched(
                analyzeResult.processedTerm(), analyzeResult.performanceType()).stream()
            .map(result -> PerformanceInfo.of(result, performanceFileService.getFileInfo(result)))
            .toList();

        performances.addAll(searchedPerformances);
        searchTermService.write(performances);

        Map<Long, Boolean> performanceFavorites = performances.stream()
            .collect(Collectors.toMap(
                PerformanceInfo::id,
                p -> false
            ));

        if (UserContext.exists()) {
            List<PerformanceInfo> favoritePerformances = performanceService.getFavoritePerformancesAll(
                UserContext.get().id(), TYPE_ALL);
            getPerformanceFavorites(performanceFavorites, favoritePerformances);
        }

        List<ConfetiSong> songs = artist.map(confetiArtist ->
                songMusicAPIService.getPopularSongsByArtist(confetiArtist.getId(),
                    RELATED_POPULAR_SONG_LIMIT))
            .orElse(List.of());

        return SearchResultDTO.of(artist.orElse(null), artistFavorite,
            performances.stream().toList(),
            performanceFavorites,
            songs);
    }

    public PopularTermsDTO getPopularSearchTerms(int limit) {
        return PopularTermsDTO.from(searchTermService.getPopularSearchTerms(limit));
    }

    private ConfetiArtist getArtistById(String aid) {
        List<ConfetiArtist> artists = artistMusicAPIService.getList(MusicAPICondition.from(aid));

        if (artists.isEmpty()) {
            log.warn("SearchFacade.getArtistById : 아티스트 아이디에 해당하는 아티스트가 존재하지 않습니다. 아이디 : {}", aid);
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        return artists.getFirst();
    }

    private void getPerformanceFavorites(Map<Long, Boolean> performanceFavorites,
        List<PerformanceInfo> favoritePerformances) {
        favoritePerformances.forEach(performance -> {
            performanceFavorites.replace(performance.id(), true);
        });
    }

    private List<ConfetiSong> getPopularSongsByPerformanceArtists(PerformanceInfo performance) {
        List<PerformanceArtistDTO> artists = performance.artists();
        if (artists == null || artists.isEmpty()) {
            return List.of();
        }

        List<String> artistIds = artists.stream()
            .map(PerformanceArtistDTO::artistId)
            .distinct()
            .limit(RELATED_POPULAR_SONG_ARTIST_LIMIT)
            .toList();

        List<ConfetiSong> mergedSongs = artistIds.stream()
            .map(artistId -> songMusicAPIService.getPopularSongsByArtist(artistId,
                RELATED_POPULAR_SONG_LIMIT))
            .flatMap(List::stream)
            .collect(Collectors.collectingAndThen(
                Collectors.toMap(
                    ConfetiSong::getId,
                    song -> song,
                    (left, right) -> left,
                    LinkedHashMap::new
                ),
                map -> new ArrayList<>(map.values())
            ));

        if (mergedSongs.isEmpty()) {
            return List.of();
        }

        Collections.shuffle(mergedSongs);
        return mergedSongs.stream()
            .limit(RELATED_POPULAR_SONG_LIMIT)
            .toList();
    }
}

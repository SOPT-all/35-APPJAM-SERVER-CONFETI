package org.sopt.confeti.api.search.facade;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.search.facade.dto.response.PopularTermsDTO;
import org.sopt.confeti.api.search.facade.dto.response.SearchResultDTO;
import org.sopt.confeti.domain.artist_favorite.application.ArtistFavoriteService;
import org.sopt.confeti.domain.concert_favorite.application.ConcertFavoriteService;
import org.sopt.confeti.domain.elastic_search.application.PerformanceSearchService;
import org.sopt.confeti.domain.elastic_search.application.SearchTermService;
import org.sopt.confeti.domain.festival_favorite.application.FestivalFavoriteService;
import org.sopt.confeti.domain.view.performance.application.PerformanceService;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.annotation.ReadOnlyTransactional;
import org.sopt.confeti.global.common.constant.PerformanceType;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.interceptor.auth.UserContext;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.util.analyzer.SearchTermAnalyzer;
import org.sopt.confeti.global.util.analyzer.dto.PerformanceSearchTermAnalyzeResult;
import org.sopt.confeti.global.util.music.MusicAPIHandler;

@Facade
@RequiredArgsConstructor
public class SearchFacade {

    private static final String TYPE_ALL = "ALL";
    private static final int ARTIST_SEARCH_COUNT = 1;

    private final SearchTermService searchTermService;
    private final MusicAPIHandler musicAPIHandler;
    private final ArtistFavoriteService artistFavoriteService;
    private final PerformanceService performanceService;
    private final FestivalFavoriteService festivalFavoriteService;
    private final ConcertFavoriteService concertFavoriteService;
    private final PerformanceSearchService performanceSearchService;

    @ReadOnlyTransactional
    public SearchResultDTO getHomeSearchResultWithAid(String aid) {
        ConfetiArtist artist = getArtistById(aid);
        searchTermService.write(artist.getName());
        boolean artistFavorite = false;

        if (UserContext.exists()) {
            artistFavorite = artistFavoriteService.isFavorite(UserContext.get().id(), aid);
        }

        List<PerformanceDTO> performances = performanceService.getPerformancesByArtistIdAndType(aid,
            PerformanceType.PERFORMANCE);

        Map<Long, Boolean> performanceFavorites = performances.stream()
            .collect(Collectors.toMap(
                PerformanceDTO::id,
                p -> false
            ));

        if (UserContext.exists()) {
            List<PerformanceDTO> favoritePerformances = performanceService.getFavoritePerformancesAll(
                UserContext.get().id(), TYPE_ALL);
            getPerformanceFavorites(performanceFavorites, favoritePerformances);
        }

        return SearchResultDTO.of(artist, artistFavorite, performances, performanceFavorites);
    }

    @ReadOnlyTransactional
    public SearchResultDTO getHomeSearchResultWithPid(long pid) {
        PerformanceDTO performance = performanceService.getPerformance(pid);
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

        return SearchResultDTO.of(performance, performanceFavorite);
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
        Set<PerformanceDTO> performances = new HashSet<>();

        // 아티스트 기반
        artist.ifPresent(confetiArtist -> performances.addAll(
                performanceService.getPerformancesByArtistIdAndType(confetiArtist.getId(),
                    analyzeResult.performanceType())
            )
        );

        // 검색어 기반
        List<PerformanceDTO> searchedPerformances = performanceSearchService.getUpcomingPerformancesByTitleAndTypePartialMatched(
                analyzeResult.processedTerm(), analyzeResult.performanceType()).stream()
            .map(PerformanceDTO::from)
            .toList();

        performances.addAll(searchedPerformances);
        searchTermService.write(performances);

        Map<Long, Boolean> performanceFavorites = performances.stream()
            .collect(Collectors.toMap(
                PerformanceDTO::id,
                p -> false
            ));

        if (UserContext.exists()) {
            List<PerformanceDTO> favoritePerformances = performanceService.getFavoritePerformancesAll(
                UserContext.get().id(), TYPE_ALL);
            getPerformanceFavorites(performanceFavorites, favoritePerformances);
        }

        return SearchResultDTO.of(artist.orElse(null), artistFavorite,
            performances.stream().toList(),
            performanceFavorites);
    }

    public PopularTermsDTO getPopularSearchTerms(int limit) {
        return PopularTermsDTO.from(searchTermService.getPopularSearchTerms(limit));
    }

    private ConfetiArtist getArtistById(String aid) {
        return musicAPIHandler.findArtistByArtistId(aid)
            .orElseThrow(
                () -> new NotFoundException(ErrorMessage.NOT_FOUND)
            );
    }

    private void getPerformanceFavorites(Map<Long, Boolean> performanceFavorites,
        List<PerformanceDTO> favoritePerformances) {
        favoritePerformances.forEach(performance -> {
            performanceFavorites.replace(performance.id(), true);
        });
    }
}

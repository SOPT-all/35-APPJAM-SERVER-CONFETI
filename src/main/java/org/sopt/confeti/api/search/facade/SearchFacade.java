package org.sopt.confeti.api.search.facade;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.sopt.confeti.api.search.facade.dto.response.PopularTermsDTO;
import org.sopt.confeti.api.search.facade.dto.response.SearchResultDTO;
import org.sopt.confeti.domain.artist_favorite.application.ArtistFavoriteService;
import org.sopt.confeti.domain.concert_favorite.application.ConcertFavoriteService;
import org.sopt.confeti.domain.elastic_search.application.PerformanceSearchService;
import org.sopt.confeti.domain.elastic_search.application.SearchTermService;
import org.sopt.confeti.domain.festival_favorite.application.FestivalFavoriteService;
import org.sopt.confeti.domain.performance.Performance;
import org.sopt.confeti.domain.performance.PerformanceType;
import org.sopt.confeti.domain.performance.application.PerformanceService;
import org.sopt.confeti.domain.performance_favorite.PerformanceFavorite;
import org.sopt.confeti.domain.performance_favorite.application.PerformanceFavoriteService;
import org.sopt.confeti.domain.view.performance.application.PerformanceService_DPRECATED;
import org.sopt.confeti.domain.view.performance.application.dto.response.PerformanceDTO;
import org.sopt.confeti.global.annotation.Facade;
import org.sopt.confeti.global.common.constant.PerformanceType_DEPRECATED;
import org.sopt.confeti.global.exception.NotFoundException;
import org.sopt.confeti.global.message.ErrorMessage;
import org.sopt.confeti.global.resolver.music_api.artist.vo.ConfetiArtist;
import org.sopt.confeti.global.util.S3FileHandler;
import org.sopt.confeti.global.util.analyzer.SearchTermAnalyzer;
import org.sopt.confeti.global.util.analyzer.dto.PerformanceSearchTermAnalyzeResult;
import org.sopt.confeti.global.util.music.MusicAPIHandler;
import org.springframework.transaction.annotation.Transactional;

@Facade
@RequiredArgsConstructor
public class SearchFacade {

    private static final String TYPE_ALL = "ALL";
    private static final int ARTIST_SEARCH_COUNT = 1;

    private final SearchTermService searchTermService;
    private final MusicAPIHandler musicAPIHandler;
    private final ArtistFavoriteService artistFavoriteService;
    private final PerformanceService_DPRECATED performanceServiceDPRECATED;
    private final FestivalFavoriteService festivalFavoriteService;
    private final ConcertFavoriteService concertFavoriteService;
    private final PerformanceSearchService performanceSearchService;
    private final PerformanceService performanceService;
    private final PerformanceFavoriteService performanceFavoriteService;
    private final S3FileHandler s3FileHandler;

    /**
     * 특정 아티스트 검색
     * 검색 결과 : 특정 아티스트 정보, 해당 아티스트와 관련한 공연 목록
     * 유저 아이디에 따라 좋아요 여부를 함께 반환
     * @param userId
     * @param aid
     * @return SearchResultDTO
     */
    @Transactional(readOnly = true)
    public SearchResultDTO getHomeSearchResultWithAid(Long userId, String aid) {
        ConfetiArtist artist = getArtistById(aid);
        searchTermService.write(artist.getName());
        boolean artistFavorite = false;

        if (Objects.nonNull(userId)) {
            artistFavorite = artistFavoriteService.isFavorite(userId, aid);
        }

        List<Performance> performances = performanceService.getRecentPerformancesByArtistId(aid);
        List<Long> performanceIds = performances.stream()
                .map(Performance::getId)
                .toList();

        Set<Long> favoritePerformanceIds = new HashSet<>();

        if (Objects.nonNull(userId)) {
            favoritePerformanceIds.addAll(performanceFavoriteService.getFavoritePerformanceIdsByPerformanceIds(userId, performanceIds));
        }

        return SearchResultDTO.of(artist, artistFavorite, performances, favoritePerformanceIds, s3FileHandler);
    }

    @Transactional(readOnly = true)
    public SearchResultDTO getHomeSearchResultWithPid(Long userId, long pid) {
        PerformanceDTO performance = performanceServiceDPRECATED.getPerformance(pid);
        searchTermService.write(performance.title());
        boolean performanceFavorite = false;

        if (Objects.nonNull(userId)) {
            if (performance.type() == PerformanceType_DEPRECATED.FESTIVAL) {
                performanceFavorite = festivalFavoriteService.isFavorite(userId, performance.typeId());
            }

            if (performance.type() == PerformanceType_DEPRECATED.CONCERT) {
                performanceFavorite = concertFavoriteService.isFavorite(userId, performance.typeId());
            }
        }

        return SearchResultDTO.of(performance, performanceFavorite);
    }

    @Transactional(readOnly = true)
    public SearchResultDTO getHomeSearchResultWithTerm(Long userId, String term) {
        PerformanceSearchTermAnalyzeResult analyzeResult = SearchTermAnalyzer.analyzePerformance(term);

        Optional<ConfetiArtist> artist = musicAPIHandler.findArtistByKeyword(analyzeResult.processedTerm());
        searchTermService.write(artist);
        boolean artistFavorite = false;

        if (Objects.nonNull(userId) && artist.isPresent()) {
            artistFavorite = artistFavoriteService.isFavorite(userId, artist.get().getId());
        }

        // 공연은 아티스트 + 검색어 기반
        Set<PerformanceDTO> performances = new HashSet<>();

        // 아티스트 기반
        artist.ifPresent(confetiArtist -> performances.addAll(
                        performanceServiceDPRECATED.getPerformancesByArtistIdAndType(confetiArtist.getId(),
                                analyzeResult.performanceTypeDEPRECATED())
                )
        );

        // 검색어 기반
        List<PerformanceDTO> searchedPerformances = performanceSearchService.getExpectedPerformancesByTitleAndTypePartialMatched(
                        analyzeResult.processedTerm(), analyzeResult.performanceTypeDEPRECATED()).stream()
                .map(PerformanceDTO::from)
                .toList();

        performances.addAll(searchedPerformances);
        searchTermService.write(performances);

        Map<Long, Boolean> performanceFavorites = performances.stream()
                .collect(Collectors.toMap(
                        PerformanceDTO::id,
                        p -> false
                ));

        if (Objects.nonNull(userId)) {
            List<PerformanceDTO> favoritePerformances = performanceServiceDPRECATED.getFavoritePerformancesAll(userId, TYPE_ALL);
            getPerformanceFavorites(performanceFavorites, favoritePerformances);
        }

        return SearchResultDTO.of(artist.orElse(null), artistFavorite, performances.stream().toList(),
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
